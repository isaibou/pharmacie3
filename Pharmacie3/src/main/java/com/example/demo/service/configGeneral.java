
  package com.example.demo.service;
  
  import java.util.Properties;
  
  import org.springframework.context.annotation.Bean; 
  import org.springframework.context.annotation.Configuration;
  import org.springframework.mail.javamail.JavaMailSender;
  import  org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
  
  
  @Configuration 
  public class configGeneral {
  
  
  
	  @Bean
		public PasswordEncoder passwordEncoder() {
			return new  BCryptPasswordEncoder(10);
		}
  
  @Bean 
  public JavaMailSender getJavaMailSender() { JavaMailSenderImpl
  mailSender = new JavaMailSenderImpl(); mailSender.setHost("smtp.gmail.com");
  mailSender.setPort(587);
  
  mailSender.setUsername("suptechmiage2018@gmail.com");
  mailSender.setPassword("miage2018*");
  
  Properties props = mailSender.getJavaMailProperties();
  props.put("mail.transport.protocol", "smtp");
  props.put("mail.smtp.auth","true"); 
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.debug", "true");
  
  return mailSender; }
  
  }
  
 