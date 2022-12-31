package io.hugo.wallet.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WalletStorageApp

fun main(args: Array<String>) {
    runApplication<WalletStorageApp>(*args)
}

