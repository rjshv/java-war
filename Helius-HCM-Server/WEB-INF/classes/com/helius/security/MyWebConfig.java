/**
 * 
 */
package com.helius.security;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Tirumala
 * 28-Dec-2018
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.helius.security")
public class MyWebConfig extends WebMvcConfigurerAdapter{

	/**
	 * 
	 */
	public MyWebConfig() {
		// TODO Auto-generated constructor stub
	}
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }

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
	  
	  @Bean
	  public UserService getuserService() {
		 return  new UserServiceImpl();
	  }
}
