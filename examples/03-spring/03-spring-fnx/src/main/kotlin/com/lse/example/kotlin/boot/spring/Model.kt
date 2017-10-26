package com.lse.example.kotlin.boot.spring

import java.time.LocalDateTime


data class Message(val message : String, val modifiedDateTime : LocalDateTime = LocalDateTime.now())