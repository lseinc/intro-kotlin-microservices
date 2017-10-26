package com.lse.example.kotlin.boot.spring

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

@RestController
@RequestMapping("/v1")
class HelloController {


    @GetMapping("/hello")
    fun hello(): Message {
        return Message("Hello World !!!")
    }

    @GetMapping("/hello/{input}")
    fun hello(@PathVariable("input") input: String): Message {
        return Message("Hello $input !!!")
    }
}

data class Message(val text : String)