package com.breakingrules.stock.productos.repository;

import com.breakingrules.stock.productos.entity.Color;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Integer> {
    boolean existsByProductoIdAndColorAndTalle(
            Integer productoId,
            Color color,
            Talle talle
    );

    boolean existsByProductoIdAndColorAndTalleAndIdNot(
            Integer productoId,
            Color color,
            Talle talle,
            Integer id
    );

    List<VarianteProducto> findByStockLessThanEqual(Integer limite);

    List<VarianteProducto> findByActivoTrue();

    @Query("SELECT COUNT(v) FROM VarianteProducto v WHERE v.stock <= :umbral AND v.activo = true")
    Integer stockBajo(@Param("umbral") Integer umbral);

    @Query("""
    SELECT v
    FROM VarianteProducto v
    WHERE v.stock <= :limite
    ORDER BY v.stock ASC
    """)
        List<VarianteProducto> productosStockBajo(@Param("limite") Integer limite);

    Optional<VarianteProducto> findByProductoIdAndColorAndTalle(
            Integer productoId,
            Color color,
            Talle talle
    );

    List<VarianteProducto> findByProductoIdAndColor(Integer productoId, Color color);

    boolean existsByCodigoBarrasAndIdNot(String codigo, Integer id);

    Optional<VarianteProducto> findByCodigoBarras(String codigoBarras);

    Optional<VarianteProducto> findByCodigoBarrasReal(String codigo);
}
