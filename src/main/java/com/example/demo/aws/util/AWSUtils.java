package com.example.demo.aws.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

public class AWSUtils {

    private static String accessKey = "AKIAJDK62XTC6UUU5ZAA";
    private static String secretKey = "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";
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
                    .partitionKey("test") // We use the ticker symbol as the partition key, explained in the Supplemental Information section below.
                    .streamName(streamName)
                    .data(SdkBytes.fromByteArray(objectDataToString.getBytes()))
                    .build();

                getKinesisClient().putRecord(request);


        } catch (JsonProcessingException | KinesisException e) {
            e.printStackTrace();
        }
    }
}
