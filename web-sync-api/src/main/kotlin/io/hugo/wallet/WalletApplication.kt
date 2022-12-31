package io.hugo.wallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["io.hugo.wallet.dal"])
@EntityScan(basePackages = ["io.hugo.wallet.model"])
@EnableTransactionManagement
class WalletSyncApplication

fun main(args: Array<String>) {
    runApplication<WalletSyncApplication>(*args)
}
