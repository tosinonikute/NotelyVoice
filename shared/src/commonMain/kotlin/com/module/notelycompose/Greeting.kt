package com.module.notelycompose

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${"Lorem Ipsum"}!"
    }
}