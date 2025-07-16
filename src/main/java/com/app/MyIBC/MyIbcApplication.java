package com.app.MyIBC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
public class MyIbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyIbcApplication.class, args);
	}

}
