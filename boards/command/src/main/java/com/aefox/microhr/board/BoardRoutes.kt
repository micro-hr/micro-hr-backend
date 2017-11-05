package com.aefox.microhr.board

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
class BoardRoutes(private val boardHandler: BoardHandler) {

    @Bean
    fun apis() = router {
        (accept(APPLICATION_JSON) and "/boards").nest {
            GET("/", boardHandler::getBoards)
            POST("/", boardHandler::addBoard)
            GET("/{id}", boardHandler::getBoard)
            PUT("/{id}", boardHandler::updateBoard)
            DELETE("/{id}", boardHandler::deleteBoard)
        }
    }

}
