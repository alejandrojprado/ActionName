package com.mahisoft.kamino.mssearch.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "crcEntityManagerFactory", transactionManagerRef = "crcTransactionManager",
        basePackages = ["com.mahisoft.kamino.mssearch.service.crc.repository"])
class CRCDbConfig {
    @Bean(name = ["crcDataSource"])
    @ConfigurationProperties(prefix = "crc.datasource")
    fun dataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["crcEntityManagerFactory"])
    fun crcEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("crcDataSource") dataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean {
        val em = builder
                .dataSource(dataSource)
                .packages("com.mahisoft.kamino.mssearch.service.crc.domain")
                .persistenceUnit("crc")
                .build()
        em.jpaPropertyMap = mapOf("hibernate.dialect" to "org.hibernate.dialect.MySQLDialect")
        return em
    }

    @Bean(name = ["crcTransactionManager"])
    fun crcTransactionManager(
        @Qualifier("crcEntityManagerFactory") crcEntityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(crcEntityManagerFactory)
    }
}