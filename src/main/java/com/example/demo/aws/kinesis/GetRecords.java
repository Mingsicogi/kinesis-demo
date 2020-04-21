package com.example.demo.aws.kinesis;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.CommonUtils;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;

import java.util.ArrayList;
import java.util.List;

public class GetRecords {
    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    GetRecords <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream to read from (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    GetRecords streamName\n";

        if (!CommonUtils.checkProgramArgs(args, 1)) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String streamName = args[0];

        getStockTrades(AWSUtils.getKinesisClient(),streamName);
    }

    // snippet-start:[kinesis.java2.getrecord.main]
    public static void getStockTrades(KinesisClient kinesisClient, String streamName) {

        String shardIterator;
        String lastShardId = null;

        // Retrieve the shards from a data stream
        DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                .streamName(streamName)
                .build();
        List<Shard> shards = new ArrayList<>();

        DescribeStreamResponse streamRes;
        do {
            streamRes = kinesisClient.describeStream(describeStreamRequest);
            shards.addAll(streamRes.streamDescription().shards());

            if (shards.size() > 0) {
                lastShardId = shards.get(shards.size() - 1).shardId();
            }
        } while (streamRes.streamDescription().hasMoreShards());

        GetShardIteratorRequest itReq = GetShardIteratorRequest.builder()
                .streamName(streamName)
                .shardIteratorType("TRIM_HORIZON")
                .shardId(shards.get(0).shardId())
                .build();

        GetShardIteratorResponse shardIteratorResult = kinesisClient.getShardIterator(itReq);
        shardIterator = shardIteratorResult.shardIterator();

        // Continuously read data records from a shard
        List<Record> records;

        // Create a GetRecordsRequest with the existing shardIterator,
        // and set maximum records to return to 1000
        GetRecordsRequest recordsRequest = GetRecordsRequest.builder()
                .shardIterator(shardIterator)
                .limit(1000)
                .build();

        GetRecordsResponse result = kinesisClient.getRecords(recordsRequest);

        // Put result into a record list, result might be empty
        records = result.records();

        // Print records
        for (Record record : records) {
            SdkBytes byteBuffer = record.data();
            System.out.println(String.format("Seq No: %s - %s", record.sequenceNumber(),
                    new String(byteBuffer.asByteArray())));
        }

    }
}
