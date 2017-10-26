package com.lse.example.kotlin.boot.spring


import org.springframework.http.MediaType.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class Routes(private val userHandler: UserHandler) {
    fun router() = router {
        accept(TEXT_HTML).nest {
            GET("/hello", {
                ServerResponse.ok().body(Mono.just(Message("Hello World")))
            })
        }
        accept(APPLICATION_JSON).nest {
            GET("/hello", {
                ServerResponse.ok().body(Mono.just(Message("Hello World")))
            })
        }
        accept(APPLICATION_JSON).nest {
                GET("/users", userHandler::findAll)
        }
//        accept(TEXT_HTML).nest {
//            GET("/users", userHandler::findAllView)
//        }
//        "/api".nest {
//            accept(APPLICATION_JSON).nest {
//                GET("/users", userHandler::findAll)
//            }
//            accept(TEXT_EVENT_STREAM).nest {
//                GET("/users", userHandler::stream)
//            }
//        }
//        }
    }
}
