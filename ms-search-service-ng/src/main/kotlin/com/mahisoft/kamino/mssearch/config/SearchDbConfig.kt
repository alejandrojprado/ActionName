package com.mahisoft.kamino.mssearch.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
        basePackages = ["com.mahisoft.kamino.mssearch.service.search.repository"])
class SearchDbConfig {
    @FlywayDataSource
    @Primary
    @Bean(name = ["dataSource"])
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Primary
    @Bean(name = ["entityManagerFactory"])
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("dataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean {
        val em = builder
                .dataSource(dataSource)
                .packages("com.mahisoft.kamino.mssearch.service.search.domain")
                .persistenceUnit("search")
                .build()
        em.jpaPropertyMap = mapOf("hibernate.dialect" to "org.hibernate.dialect.MySQLDialect")
        return em
    }

    @Primary
    @Bean(name = ["transactionManager"])
    fun transactionManager(
        @Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}