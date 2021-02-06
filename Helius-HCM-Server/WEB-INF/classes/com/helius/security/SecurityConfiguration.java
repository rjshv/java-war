package com.helius.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.helius.entities.User;


 
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    private static String REALM="MY_TEST_REALM";
    
     @Autowired
	 ApplicationContext context;
  
     @Autowired
     MyWebConfig webconfig;
     
    
     UserService userService;
    
     //AuthenticationManagerBuilder auth;
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    	/*this.auth = auth1;
        this.auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("ADMIN");
        this.auth.inMemoryAuthentication().withUser("tom").password("abc123").roles("USER");*/
    	 auth.userDetailsService(inMemoryUserDetailsManager());
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  
      //http.csrf().disable()
 
    	http.csrf().disable().authorizeRequests()
        .antMatchers("/user/createuser").hasAnyRole("HR-superuser")
        .antMatchers("/user/updateuser").hasAnyRole("HR-superuser")
        .antMatchers("/client/save").hasAnyRole("HR-superuser,Accountmanager")
        .antMatchers("/client/update").hasAnyRole("HR-superuser,Accountmanager")
      //  .anyRequest().authenticated()
        .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//We don't need session.
     //http.cors();
    }
   
    
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        /*final Properties users = new Properties();
        users.put("user","pass,ROLE_USER,enabled"); //add whatever other user you need
*/        userService = webconfig.getuserService();
        DataSource datasource = webconfig.getDataSource();
        userService.populateDummyUsers(datasource);
        List<com.helius.entities.User> allusers = userService.findAllUsers();
        List<UserDetails> springusers = new ArrayList<UserDetails>();
        if(allusers != null) {
        	for(User user : allusers){
        		
        		String decodedpassword = new String(Base64.getDecoder().decode(user.getPassword()));
        		String userid = user.getUserid();
        		String[] rr = user.getRole().split(",");
				List<GrantedAuthority> role = new ArrayList<GrantedAuthority>();
				for (int i = 0; i < rr.length; i++) {
					GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+rr[i]);
					role.add(ga);
				}			
				int loginAttempt = Integer.parseInt(user.getUserLoginAttempts());
				if(loginAttempt !=0){
				org.springframework.security.core.userdetails.User user_sec =
        				new org.springframework.security.core.userdetails.User(user.getUserid(),decodedpassword,true,true,true,true,role);
				springusers.add(user_sec);
				}
        		//this.auth.inMemoryAuthentication().withUser(user.getUserid()).password(decodedpassword).roles(user.getRole());
        	}
        }
        
        System.out.println("Loaded the users in SecurityConfiguration");
        
        return new InMemoryUserDetailsManager(springusers);
    }
    
    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }
     
    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring().antMatchers("/verifyEmailAddress/");
        web.ignoring().antMatchers("/resetPwd/");
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = new ArrayList<String>();
        origins.add("*");
        
        List<String> methods = new ArrayList<String>();
        methods.add("HEAD");
        methods.add("GET");
        methods.add("POST");
        methods.add("PUT");
        methods.add("DELETE");
        methods.add("PATCH");
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(methods);
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        List<String> headers = new ArrayList();
        headers.add("Authorization");
        headers.add("Cache-Control");
        headers.add("Content-Type");
        
        configuration.setAllowedHeaders(headers);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
   
}