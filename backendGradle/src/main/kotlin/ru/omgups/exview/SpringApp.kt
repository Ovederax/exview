package ru.omgups.exview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringApp

fun main(args: Array<String>) {
    runApplication<SpringApp>(*args)
}