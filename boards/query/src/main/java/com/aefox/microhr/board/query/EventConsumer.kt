package com.aefox.microhr.board.query

import com.aefox.microhr.board.events.BoardCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EventConsumer {

    @KafkaListener(topics = arrayOf("boardEventsTopic"))
    fun onReceiving(event: BoardCreatedEvent) {
        log.info("received = {}", event)
    }

    companion object {
        private val log = LoggerFactory.getLogger(EventConsumer::class.java)
    }
}
