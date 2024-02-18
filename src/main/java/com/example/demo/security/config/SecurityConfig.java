package com.example.demo.security.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.validation.CustomTokenValidationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomTokenValidationFilter customTokenValidationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable().requestMatchers().and().authorizeRequests().anyRequest().permitAll().antMatchers("/**")
        .authenticated().and().addFilterAfter(customTokenValidationFilter, BasicAuthenticationFilter.class);
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {}
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }
}
