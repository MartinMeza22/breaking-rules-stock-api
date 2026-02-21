package com.brakingrules.stock.repository;

import com.brakingrules.stock.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}