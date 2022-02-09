package com.fowlart.weatherkafkaproducer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final String weatherApiKey;
    private final String weatherApiLocation;
    private final String kafkaUser;
    private final String kafkaKey;
    private final KafkaProducer<String, String> kafkaProducer;

    public ScheduledTasks(@Value("${weather.api.key}") String weatherApiKey,
                          @Value("${weather.api.location}") String weatherApiLocation,
                          @Value("${kafka.cluster.username}") String kafkaUser,
                          @Value("${kafka.cluster.key}") String kafkaKey) {
        log.info("Using "+weatherApiLocation+" as location");
        this.weatherApiKey = weatherApiKey;
        this.weatherApiLocation = weatherApiLocation;
        this.kafkaUser = kafkaUser;
        this.kafkaKey = kafkaKey;
        kafkaProducer = getStringStringKafkaProducer();
    }

    public KafkaProducer<String, String> getStringStringKafkaProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-lq8gm.westeurope.azure.confluent.cloud:9092");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty("security.protocol", "SASL_SSL");
        String w = "org.apache.kafka.common.security.plain.PlainLoginModule required username='" + kafkaUser + "' password='" + kafkaKey + "';";
        log.info(">>> Connection string: " + w);
        properties.setProperty("sasl.jaas.config", w);
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("client.dns.lookup", "use_all_dns_ips");
        properties.setProperty("session.timeout.ms", "45000");
        //    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group_0");
        return new KafkaProducer<>(properties);
    }

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("http://api.weatherapi.com/v1/current.json?key=" + weatherApiKey + "&q="
                        + weatherApiLocation + "&aqi=no")
                .method("GET", null)
                .build();

        Response response = null;

        response = client.newCall(request).execute();
        String weatherJson = response.body().string();
        log.info(weatherJson);
        ProducerRecord<String, String> record = new ProducerRecord<>("weather", weatherJson);
        kafkaProducer.send(record);
        kafkaProducer.flush();
    }
}
