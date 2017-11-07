package com.aefox.microhr.board.spring

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import java.util.*

@Configuration
class KafkaProducerConfig {

    @Value("\${kafka.producer.bootstrap}")
    private val bootstrapServers: String? = null

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> =
        DefaultKafkaProducerFactory(producerConfigs())

    @Bean
    fun producerConfigs(): Map<String, Any?> {
        val props = HashMap<String, Any?>()

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)

        return props
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        val kafkaTemplate = KafkaTemplate<String, Any>(producerFactory())
        kafkaTemplate.defaultTopic = "boardEventsTopic"
        kafkaTemplate.setMessageConverter(StringJsonMessageConverter())
        return kafkaTemplate
    }
}

