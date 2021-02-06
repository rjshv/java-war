/**
 * 
 */
package com.helius.security;

import java.util.Properties;

/**
 * @author Tirumala
 * 20-Nov-2018
 */
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

@Configuration
@PropertySource("db.properties")
@ComponentScan("com.helius.security")

public class AppConfig {

  @Autowired
  private Environment env;

  @Bean
  public DataSource getDataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(env.getProperty("mysql.driver"));
    dataSource.setUrl(env.getProperty("mysql.jdbcUrl"));
    dataSource.setUsername(env.getProperty("mysql.username"));
    dataSource.setPassword(env.getProperty("mysql.password"));
    return dataSource;
  }
  
 /* @SuppressWarnings("deprecation")
@Bean
  public LocalSessionFactoryBean localSessionFactoryBean() {
	  LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
	  localSessionFactoryBean.setDataSource(getDataSource());
	  Properties prop = new Properties();
	  prop.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	  prop.put("hibernate.show_sql", false);
		  
	  localSessionFactoryBean.setHibernateProperties(prop);
	  return localSessionFactoryBean;
  }*/
}
