package com.mahisoft.kamino.mssearch.config

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.mahisoft.kamino.mssearch.service"])
class Config {

    @Value("\${elasticsearch.cluster.ip: #{null}}")
    private val clusterIp: String? = null

    @Value("\${elasticsearch.cluster.port: #{null}}")
    private val clusterPort: Int? = null

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(name = ["elasticsearch.cluster.ip", "elasticsearch.cluster.port"])
    fun client(): RestHighLevelClient? {
        return RestHighLevelClient(
                RestClient.builder(HttpHost(clusterIp, clusterPort!!)))
    }
}