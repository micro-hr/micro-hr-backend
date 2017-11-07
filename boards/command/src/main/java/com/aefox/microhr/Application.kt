package com.aefox.microhr

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = arrayOf("com.aefox.microhr.board"))
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
