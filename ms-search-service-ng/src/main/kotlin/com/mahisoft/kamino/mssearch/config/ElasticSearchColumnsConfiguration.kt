package com.mahisoft.kamino.mssearch.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "columns-config", ignoreUnknownFields = false)
data class ElasticSearchColumnsConfiguration(
    val containedKeys: List<String>
)