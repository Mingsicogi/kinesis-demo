package com.example.demo.aws.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;

import java.util.List;

public class AWSUtils {

    private static String accessKey = "AKIAJMGCW3123GE2323FXLNV7DA";
    private static String secretKey = "Zj22oGmIhh8vTiGDyCv232323qDQD123fGXLYMesphqqqXQHp";
    private static Region region = Region.AP_NORTHEAST_2;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static AwsCredentialsProvider getCredential() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    public static KinesisClient getKinesisClient() {
       return KinesisClient.builder()
                .region(region)
                .credentialsProvider(getCredential())
                .build();
    }

    public static KinesisAsyncClient getKinesisAsyncClient() {
        return KinesisAsyncClient.builder().region(region).credentialsProvider(getCredential()).build();
    }

    public static boolean validateStream(String streamName) {
        try {
            DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                    .streamName(streamName)
                    .build();

            DescribeStreamResponse describeStreamResponse = getKinesisClient().describeStream(describeStreamRequest);

            if(!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }
        }catch (KinesisException e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);

            return false;
        }

        return true;
    }

    public static void sendDataToStream(Object data, String streamName) {
        try {
            String objectDataToString = objectMapper.writeValueAsString(data);

            PutRecordRequest request = PutRecordRequest.builder()
                    .partitionKey("651243651423kjsadhfkjhasdfkjhasd^^^!@*#&^!@*&#^*!@&#^*!@&#^*!@&#^0") // We use the ticker symbol as the partition key, explained in the Supplemental Information section below.
                    .streamName(streamName)
                    .data(SdkBytes.fromByteArray(objectDataToString.getBytes()))
                    .build();

                getKinesisClient().putRecord(request);


        } catch (JsonProcessingException | KinesisException e) {
            e.printStackTrace();
        }
    }


    public static List<Shard> listKinShards(KinesisClient kinesisClient, String name) {

        try {
            ListShardsRequest request = ListShardsRequest.builder()
                .streamName(name)
                .build();

            ListShardsResponse response = kinesisClient.listShards(request);

            return response.shards();
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        throw new RuntimeException("");
    }
}
