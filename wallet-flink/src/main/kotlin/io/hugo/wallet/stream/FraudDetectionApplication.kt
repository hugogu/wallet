@file:JvmName("FraudDetection")

package io.hugo.wallet.stream

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FraudDetectionJobApplication

fun main(args: Array<String>) {
    runApplication<FraudDetectionJobApplication>(*args) {
        this.webApplicationType = WebApplicationType.NONE
    }
}
