package com.mahisoft.kamino.mssearch.containers

import org.testcontainers.containers.MySQLContainer

class CustomMysqlContainer : MySQLContainer<CustomMysqlContainer>(IMAGE_VERSION) {
    companion object {
        const val IMAGE_VERSION = "percona:latest"
        var customMysqlContainer: CustomMysqlContainer? = null

        fun getInstance(): CustomMysqlContainer {
            if (customMysqlContainer == null) {
                customMysqlContainer = CustomMysqlContainer()
            }
            return customMysqlContainer as CustomMysqlContainer
        }
    }

    override fun stop() {}
}