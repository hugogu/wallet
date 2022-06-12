package io.hugo.wallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing

@SpringBootApplication
@EnableR2dbcAuditing
@EnableJpaRepositories
class WalletApplication

fun main(args: Array<String>) {
    runApplication<WalletApplication>(*args)
}
