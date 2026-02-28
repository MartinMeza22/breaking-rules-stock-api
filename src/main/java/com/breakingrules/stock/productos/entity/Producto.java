package com.breakingrules.stock.productos.entity;

import com.breakingrules.stock.proveedores.entity.Proveedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Entity
@Table(
        name = "productos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_producto_codigo_barras", columnNames = "codigo_barras")
        },
        indexes = {
                @Index(name = "idx_producto_nombre", columnList = "nombre"),
                @Index(name = "idx_producto_codigo_barras", columnList = "codigo_barras")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(unique = true, length = 100)
    private String sku;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotNull(message = "El talle es obligatorio")
    @Enumerated(EnumType.STRING)
    private Talle talle;

    @NotNull(message = "El color es obligatorio")
    private String color;

    @Column(nullable = false, name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;


    @Positive(message = "El costo debe ser mayor a 0")
    @Column(precision = 15, scale = 2)
    private BigDecimal costo;

    @NotNull(message = "El precio de venta es obligatorio")

    @Column(name = "precio_venta", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioVenta;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}