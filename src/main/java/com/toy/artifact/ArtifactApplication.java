package com.toy.artifact;

import ch.qos.logback.classic.Logger;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.persistence.EntityManager;

@SpringBootApplication
public class ArtifactApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtifactApplication.class, args);
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
		return new JPAQueryFactory(entityManager);
	}

}
