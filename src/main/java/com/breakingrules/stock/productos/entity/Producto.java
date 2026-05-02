package com.breakingrules.stock.productos.entity;

import com.breakingrules.stock.clientes.entity.TipoCliente;
import com.breakingrules.stock.productos.validation.PrecioValido;
import com.breakingrules.stock.proveedores.entity.Proveedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.*;
import org.hibernate.annotations.Where;

@PrecioValido
@Entity
@Table(name = "productos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El Articulo es obligatorio")
    @Column(length = 100)
    private String sku;

    private String nombre;

    @Positive(message = "El costo debe ser mayor a 0")
    private BigDecimal costo;

    @NotNull(message = "El precio público es obligatorio")
    @Positive(message = "El precio público debe ser mayor a 0")
    @Column(nullable = false)
    private BigDecimal precioBasePublico;

    @NotNull(message = "El precio mayorista es obligatorio")
    @Positive(message = "El precio mayorista debe ser mayor a 0")
    @Column(nullable = false)
    private BigDecimal precioBaseMayorista;

    @Column
    private BigDecimal precioEspecial1Publico; // XXL y XXXL

    @Column
    private BigDecimal precioEspecial1Mayorista;

    @Column
    private BigDecimal precioEspecial2Publico; // XXXXL

    @Column
    private BigDecimal precioEspecial2Mayorista;

    @Column
    private BigDecimal precioEspecial3Publico; // XXXXXL y XXXXXXL

    @Column
    private BigDecimal precioEspecial3Mayorista;

    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<VarianteProducto> variantes;

    @Enumerated(EnumType.STRING)
    private TipoTalle tipoTalle;

    @Transient
    public Integer getStockTotal() {

        if (variantes == null) {
            return 0;
        }

        return variantes.stream()
                .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
                .sum();
    }

    public boolean isTienePreciosEspecialesPublico() {
        return precioEspecial1Publico != null
                || precioEspecial2Publico != null
                || precioEspecial3Publico != null;
    }

    public boolean isTienePreciosEspecialesMayorista() {
        return precioEspecial1Mayorista != null
                || precioEspecial2Mayorista != null
                || precioEspecial3Mayorista != null;
    }

    @PrePersist
    @PreUpdate
    public void syncNombreYVariantes() {
        // Tu lógica de nombre que ya tenías
        if (nombre == null || nombre.isBlank()) {
            nombre = sku;
        }

        if (variantes != null) {
            variantes.forEach(v -> v.generarCodigoBarras());
        }
    }
}