package com.brakingrules.stock;

import com.brakingrules.stock.model.Producto;
import com.brakingrules.stock.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BrakingrulesStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrakingrulesStockApplication.class, args);
	}
	@Bean
	CommandLineRunner init(ProductoRepository repo) {
		return args -> {
			repo.save(new Producto(null, "Pantalon", "Algodon", "XL", "Negro", 13000.00, 5));
			repo.save(new Producto(null, "Bermuda", "Algodon", "XXL", "Blanco", 18000.00, 5));
		};
	}
}
