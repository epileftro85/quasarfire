package com.mercadolibre.quasarfire.config;

import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.mercadolibre.quasarfire.dtos");

        return props;
    }

    @Bean
    public ConsumerFactory<String, SatelliteMessageDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
                new JsonDeserializer<>(SatelliteMessageDTO.class));
    }

    public ConcurrentMessageListenerContainer<String, SatelliteMessageDTO> factory(
            ConsumerFactory<String, SatelliteMessageDTO> consumerFactory
    ) {
        ContainerProperties containerProperties = new ContainerProperties("topsecrettopic");
        ConcurrentMessageListenerContainer<String, SatelliteMessageDTO> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener(messageListener());

        return container;
    }

    @Bean
    public MessageListener<String, SatelliteMessageDTO> messageListener() {
        return record -> {
            String key = record.key();
            SatelliteMessageDTO value = record.value();
            System.out.println("Received message: " + value);
        };
    }
}
