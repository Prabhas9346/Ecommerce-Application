package com.prabhas.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		 ApplicationContext applicationContext= SpringApplication.run(EcommerceApplication.class, args);


		System.out.println("E-commerce Application Started Successfully");
	}

}
