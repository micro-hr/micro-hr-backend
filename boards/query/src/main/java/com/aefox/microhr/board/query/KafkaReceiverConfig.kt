package com.aefox.microhr.board.query

import com.aefox.microhr.board.events.Event
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonDeserializer
import java.util.*

@Configuration
@EnableKafka
class KafkaReceiverConfig {

    @Value("\${kafka.consumer.bootstrap}")
    private val bootstrapServers: String? = null

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.setConcurrency(1)
        factory.consumerFactory = consumerFactory()
        factory.setMessageConverter(StringJsonMessageConverter())
        return factory
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> =
        DefaultKafkaConsumerFactory(consumerProps(), stringKeyDeserializer(), stringKeyDeserializer())

    @Bean
    fun consumerProps(): Map<String, Any?> {
        val props = HashMap<String, Any?>()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json")
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        return props
    }

    @Bean
    fun stringKeyDeserializer(): Deserializer<String> = StringDeserializer()

    @Bean
    fun eventJsonValueDeserializer(): Deserializer<Event> = JsonDeserializer(Event::class.java)
}

