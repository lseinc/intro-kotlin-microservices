package com.lse.example.kotlin.ktor.json

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.GsonConverter
import io.ktor.host.embeddedServer
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.netty.Netty
import io.ktor.pipeline.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


data class Model(val name: String, val items: List<Item>)
data class Item(val key: String, val value: String)


var counter = AtomicInteger()

fun incrementCounter(): String {
    return counter.incrementAndGet().toString()
}

fun log(msg: String) = println(LocalDateTime.now().toString()+" [${Thread.currentThread().name}] $msg")

suspend fun ApplicationCall.handleLongCalculation(start: Long) {
    val queue = System.currentTimeMillis() - start
    var number = 0
    val random = Random()
    for (index in 0..300) {
        delay(10)
        number += random.nextInt(100)
    }
    val time = System.currentTimeMillis() - start

    val response = Model("reactive", listOf(
            Item("start", start.toString()),
            Item("queue", queue.toString()),
            Item("counter", incrementCounter()),
            Item("threads", java.util.concurrent.ForkJoinPool.getCommonPoolParallelism().toString()),
            Item("duration", time.toString())))
    respond(response)
}

fun Application.module() {
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }

    val model = Model("root", listOf(Item("A", "Apache"), Item("B", "Bing")))
    routing {
        get("/v1") {
            log("/v1 called")
            call.respond(model)
        }
        get("/v1/item/{key}") {
            log("/v1/item/${call.parameters["key"]}")
            val item = model.items.firstOrNull { it.key == call.parameters["key"] }
            if (item == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(item)
        }
        get("/rx") {
            log("/rx")
            val start = System.currentTimeMillis()
            kotlinx.coroutines.experimental.run(CommonPool) {
                call.handleLongCalculation(start)
            }
        }

    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty,
            port = 8080,
            watchPaths = listOf("com.lse.example.kotlin.ktor.json.*"),
            module = Application::module
    ).start()
}


/*
         > curl -v --compress --header "Accept: application/json" http://localhost:8080/v1
         {"name":"root","items":[{"key":"A","value":"Apache"},{"key":"B","value":"Bing"}]}

         > curl -v --compress --header "Accept: application/json" http://localhost:8080/v1/item/A
         {"key":"A","value":"Apache"}
*/


