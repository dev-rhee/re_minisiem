package org.minisiem.reminisiem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReMinisiemApplication

fun main(args: Array<String>) {
    runApplication<ReMinisiemApplication>(*args)
}
