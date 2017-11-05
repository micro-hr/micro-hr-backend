package com.aefox.microhr.board.spring;

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("com.aefox.microhr.board")
class Config {

    @Bean
    fun transactionManager(): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory().getObject())

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setGenerateDdl(true)

        val factoryBean = LocalContainerEntityManagerFactoryBean()

        factoryBean.dataSource = dataSource()
        factoryBean.jpaVendorAdapter = vendorAdapter
        factoryBean.setPackagesToScan("com.aefox.microhr.board")

        return factoryBean
    }

    @Bean
    fun dataSource(): DataSource =
        EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .setName("boards")
            .build()
}
