package com.fowlart.weatherkafkaproducer;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AzureRESTConnectorStarter {

    private static final Logger log = LoggerFactory.getLogger(AzureRESTConnectorStarter.class);

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8083/connectors")
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        if (!"[AzureBlobStorageSourceConnector]".equals(response.toString())) {

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n  \"name\" : \"AzureBlobStorageSourceConnector\",\n  \"config\" : {\n    \"connector.class\" : \"io.confluent.connect.azure.blob.storage.AzureBlobStorageSourceConnector\",\n    \"tasks.max\" : \"1\",\n    \"azblob.account.name\" : \"bd201stacc\",\n    \"azblob.account.key\" : \"L6X661FhSG78rVrWUyNOTzeXyzfHzQVZF9bX0ukUS4U90VQdLTeuEVWUFiok8l4MAXJGMLYIolTT1R1rjuNtrA\",\n    \"azblob.container.name\" : \"m11kafkaconnect\",\n    \"format.class\" : \"io.confluent.connect.azure.blob.storage.format.avro.AvroFormat\",\n    \"confluent.topic.bootstrap.servers\" : \"pkc-lq8gm.westeurope.azure.confluent.cloud:9092\",\n    \"confluent.topic.replication.factor\" : \"1\",\n    \"confluent.topic.producer.security.protocol\": \"SASL_SSL\",\n    \"confluent.topic.producer.sasl.mechanism\":\"PLAIN\",\n    \"confluent.topic.producer.sasl.jaas.config\":\"org.apache.kafka.common.security.plain.PlainLoginModule required username=\\\"U5OY7D3DEZHLRLSK\\\" password=\\\"pWP6wZKmJOducnaK1VbKCZk9nn+KC+DwwqZSp4hY0En2t3YwtO/Io0t7EEeTXtaf\\\";\",\n    \"confluent.topic.consumer.security.protocol\": \"SASL_SSL\",\n    \"confluent.topic.consumer.sasl.mechanism\":\"PLAIN\",\n    \"confluent.topic.consumer.sasl.jaas.config\":\"org.apache.kafka.common.security.plain.PlainLoginModule required username=\\\"U5OY7D3DEZHLRLSK\\\" password=\\\"pWP6wZKmJOducnaK1VbKCZk9nn+KC+DwwqZSp4hY0En2t3YwtO/Io0t7EEeTXtaf\\\";\"\n  }\n}");
            Request request1 = new Request.Builder()
                    .url("http://localhost:8083/connectors/")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response1 = client.newCall(request1).execute();

            log.info(response1.body().string());
        }
        else {
            log.info(response.body().string());
        }
    }
}
