package com.mahisoft.kamino.mssearch

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.mahisoft.kamino", "com.mahisoft.kamino.mssearch"])
@ConfigurationPropertiesScan
class MsSearchApplication

fun main(args: Array<String>) {
    runApplication<MsSearchApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}