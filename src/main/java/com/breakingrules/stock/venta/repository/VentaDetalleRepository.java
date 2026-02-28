package com.breakingrules.stock.venta.repository;

import com.breakingrules.stock.venta.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Integer> {
}
