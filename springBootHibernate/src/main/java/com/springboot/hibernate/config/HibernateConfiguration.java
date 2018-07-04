package com.springboot.hibernate.config;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Bean
	public SessionFactory getSessionFactory() {
		if(entityManagerFactory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("SessionFactory is null");
		}
		return entityManagerFactory.unwrap(SessionFactory.class);
	}

}
