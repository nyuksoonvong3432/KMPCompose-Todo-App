package com.habitap.todoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform