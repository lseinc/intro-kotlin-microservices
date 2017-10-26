package com.lse.example.kotlin.boot.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.context.support.beans
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.resources
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver

fun beans() = beans {
        bean<UserHandler>()
        bean<Routes>()
        bean<ThymeleafReactiveViewResolver>()
        bean("webHandler") {
            RouterFunctions.toWebHandler(
                    ref<Routes>().router(),
                    HandlerStrategies.builder().viewResolver(ref())
                            .build()
            )
        }
        bean("messageSource") {
            ReloadableResourceBundleMessageSource().apply {
                setBasename("messages")
                setDefaultEncoding("UTF-8")
            }
        }
        resources("/**", ClassPathResource("static/"))
        profile("foo") {
            bean<Foo>()
        }
}

class Foo