package com.hollysys.smartfactory.kafkaproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sanelee
 */
@Configuration
public class KafkaInitialConfiguration {
    /**创建一个名为testTopic的Topic并设置分区数partitions为8，分区副本数replication-factor为2*/
    @Bean
    public NewTopic initialTopic() {
        System.out.println("begin to init initialTopic........................");
        return new NewTopic("kafka-topic",8, (short) 2 );
    }
    // 如果要修改分区数，只需修改配置值重启项目即可
    /**修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小*/
    @Bean
    public NewTopic updateTopic() {
        System.out.println("begin to init updateTopic........................");
        return new NewTopic("kafka-topic",10, (short) 2 );
    }
}
