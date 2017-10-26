package com.lse.example.kotlin.ktor.html

import io.ktor.host.embeddedServer
import io.ktor.netty.Netty
import io.ktor.routing.get
import io.ktor.routing.routing

import kotlinx.html.*
import io.ktor.html.*
import io.ktor.pipeline.*

var counter : Int = 0


fun FlowContent.widget(body: FlowContent.() -> Unit) {
    div { body() }
}

fun incrementCounter(count: Int) : String {
    counter = count + 1
    return counter.toString()
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        routing {
            get("/") {
                call.respondHtml {
                    head {
                        title { +"HTML Application" }
                    }
                    body {
                        h1 { +"Sample application with HTML builders" }
                        widget {
                            +"Widgets are just functions: "
                            + incrementCounter(counter)
                        }
                        a(href = "./") { +"reload" }
                    }
                }
            }
        }
    }.start(wait = true)
}

