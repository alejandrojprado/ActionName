package com.mahisoft.kamino.mssearch.containers

import org.testcontainers.elasticsearch.ElasticsearchContainer

class CustomElasticSearchContainer : ElasticsearchContainer(ELASTIC_SEARCH_DOCKER) {
    companion object {
        private const val ELASTIC_SEARCH_DOCKER = "elasticsearch:7.6.2"
        private const val CLUSTER_NAME = "cluster.name"
        private const val ELASTIC_SEARCH = "elasticsearch"

        var elasticSearchContainer: CustomElasticSearchContainer? = null

        fun getInstance(): CustomElasticSearchContainer {
            if (elasticSearchContainer == null) {
                elasticSearchContainer = CustomElasticSearchContainer()
            }
            return elasticSearchContainer as CustomElasticSearchContainer
        }
    }

    init {
        this.addFixedExposedPort(9200, 9200)
        this.addFixedExposedPort(9300, 9300)
        addEnv(CLUSTER_NAME, ELASTIC_SEARCH)
    }

    override fun stop() {}
}