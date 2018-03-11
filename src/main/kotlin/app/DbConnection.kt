package app

import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.context.annotation.Bean
import javax.sql.DataSource
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import java.util.*

@Configuration
class DbConnection {

    companion object {
        val jdbcUrl = System.getProperty("db.address", "localhost:3306")
        val dbName = System.getProperty("db.name", "ut_test")
        val username = System.getProperty("db.username", "root")
        val password = System.getProperty("db.password", "root")
    }

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()

        dataSource.setDriverClassName("com.mysql.jdbc.Driver")
        dataSource.url = "jdbc:mysql://$jdbcUrl/$dbName?createDatabaseIfNotExist=true"
        dataSource.username = username
        dataSource.password = password

        return dataSource
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan("app.model")

        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter

        val properties = Properties()
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update")
        em.setJpaProperties(properties)

        return em
    }
}