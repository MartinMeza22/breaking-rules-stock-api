package com.breakingrules.stock.venta.repository;

import com.breakingrules.stock.venta.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Integer> {
    List<VentaDetalle> findByVentaId(Integer id);
    Optional<VentaDetalle> findByVentaIdAndVarianteId(Integer ventaId, Integer varianteId);
    @Query("""
    SELECT d FROM VentaDetalle d
    WHERE d.venta.id = :ventaId
    AND d.variante.id = :varianteId
""")
    Optional<VentaDetalle> findDetalle(
            @Param("ventaId") Integer ventaId,
            @Param("varianteId") Integer varianteId
    );
}
