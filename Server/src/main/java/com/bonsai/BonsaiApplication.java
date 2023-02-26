package com.bonsai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BonsaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BonsaiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:8080");
				registry.addMapping("/**").allowedMethods("GET", "POST", "DELETE", "PUT");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {

				// Register resource handler for images
				registry.addResourceHandler("/resource/images/**").addResourceLocations("classpath:static/images/")
						.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
			}
		};
	}

}
