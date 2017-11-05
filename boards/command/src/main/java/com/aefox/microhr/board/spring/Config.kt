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
@EnableJpaRepositories(basePackages = arrayOf("com.aefox.microhr.board"), entityManagerFactoryRef = "boardEntityManagerFactory",
    transactionManagerRef = "boardTransactionManager")
//@EnableJpaRepositories
//@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
//    transactionManagerRef = "transactionManager")
class Config {

    @Bean
    fun boardTransactionManager(): PlatformTransactionManager  {
//    fun transactionManager(): PlatformTransactionManager  {
        return JpaTransactionManager(boardEntityManagerFactory().getObject())
    }

    @Bean
    fun boardEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
//    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setGenerateDdl(true)

        val factoryBean = LocalContainerEntityManagerFactoryBean()

        factoryBean.dataSource = boardDataSource()
        factoryBean.jpaVendorAdapter = vendorAdapter
        factoryBean.setPackagesToScan("com.aefox.microhr.board")

        return factoryBean
    }

    @Bean
    fun boardDataSource(): DataSource {
//    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .setName("boards")
            .build()
    }
}
