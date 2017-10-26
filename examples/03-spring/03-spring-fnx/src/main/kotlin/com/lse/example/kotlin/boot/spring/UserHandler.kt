package com.lse.example.kotlin.boot.spring

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Flux
import java.time.Duration.ofMillis
import java.time.LocalDate

class UserHandler {

    private val users = Flux.just(
            User("Foo", "Foo", LocalDate.now().minusDays(1)),
            User("Bar", "Bar", LocalDate.now().minusDays(10)),
            User("Baz", "Baz", LocalDate.now().minusDays(100)))

    private val userStream = Flux
            .zip(Flux.interval(ofMillis(100)), users.repeat())
            .map { it.t2 }

    fun findAll(req: ServerRequest) =
            ok().body(users)

    fun findAllView(req: ServerRequest) =
            ok().render("users", users)

    fun stream(req: ServerRequest) =
            ok().bodyToServerSentEvents(userStream)

}

data class User(val firstName: String, val lastName: String, val birthDate: LocalDate)
