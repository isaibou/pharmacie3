package com.example.demo.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	DataSource dataSource;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication()
		
		.passwordEncoder(encoder)
		.usersByUsernameQuery("select email as principal, mdp as credentials, actived from client where email =?")
	
		.authoritiesByUsernameQuery("select client_email as principal, role_role as role from client_role where client_email =?")
		.rolePrefix("ROLE_")
		.dataSource(dataSource);
		
	} 
@Override
protected void configure(HttpSecurity http) throws Exception {
	http
	.authorizeRequests()
	.antMatchers("/css/**","/js/**","/libs/**","/fonts/**","/src/**","/forgotPassword","/resetPassword","/inscription","/inscrire").permitAll()
	.anyRequest()
	.authenticated()
	.and()
	.formLogin()
	.loginPage("/LoginVrai")
	.successForwardUrl("/sai")
	.permitAll()
	
	.and()
	.logout()
	.logoutUrl("/LoginVrai?logout")
	.logoutSuccessUrl("/LoginVrai");
	
}


	
}

 

  