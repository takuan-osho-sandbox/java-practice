package com.github.takuanosho.udemy.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class StreamsStarterApp {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-starter-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KStreamBuilder builder = new KStreamBuilder();
        // 1 - stream fro Kafka
        KStream<String, String> wordCountInput = builder.stream("word-count-input");
        // 2 - map values to lowercase
        KTable<String, Long> wordCounts = wordCountInput
                .mapValues(textLine -> textLine.toLowerCase())
                .flatMapValues(lowercasedTextLine -> Arrays.asList(lowercasedTextLine.split(" ")))
                .selectKey((ignoredKey, word) -> word)
                .groupByKey()
                .count("Counts");
        
        wordCounts.to(Serdes.String(), Serdes.Long(), "word-count-output");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        // printed the topology
        System.out.println(streams.toString());

        // shutdown hook to correctly the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
