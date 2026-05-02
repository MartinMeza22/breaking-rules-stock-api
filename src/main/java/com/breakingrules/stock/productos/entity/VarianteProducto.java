package com.breakingrules.stock.productos.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Table(
        name = "variantes_producto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_producto_color_talle",
                        columnNames = {"producto_id", "color", "talle"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private Talle talle;

    @Column(name = "codigo_barras", unique = true, nullable = false)
    private String codigoBarras;

    @Column(name = "codigo_barras_real", unique = true)
    private String codigoBarrasReal;

    private Integer stock;


    @Column(nullable = false)
    private boolean activo = true;


    @Transient
    public BigDecimal getPrecioPublicoFinal() {

        if (producto == null || talle == null) {
            return BigDecimal.ZERO;
        }

        switch (talle) {

            case XXL:
            case XXXL:
                return producto.getPrecioEspecial1Publico() != null
                        ? producto.getPrecioEspecial1Publico()
                        : producto.getPrecioBasePublico();

            case XXXXL:
                if (producto.getPrecioEspecial2Publico() != null) {
                    return producto.getPrecioEspecial2Publico();
                }
                if (producto.getPrecioEspecial1Publico() != null) {
                    return producto.getPrecioEspecial1Publico();
                }
                return producto.getPrecioBasePublico();

            case XXXXXL:
            case XXXXXXL:
                if (producto.getPrecioEspecial3Publico() != null) {
                    return producto.getPrecioEspecial3Publico();
                }
                if (producto.getPrecioEspecial2Publico() != null) {
                    return producto.getPrecioEspecial2Publico();
                }
                if (producto.getPrecioEspecial1Publico() != null) {
                    return producto.getPrecioEspecial1Publico();
                }
                return producto.getPrecioBasePublico();

            default:
                return producto.getPrecioBasePublico();
        }
    }

    @Transient
    public BigDecimal getPrecioMayoristaFinal() {

        if (producto == null || talle == null) {
            return BigDecimal.ZERO;
        }

        switch (talle) {

            case XXL:
            case XXXL:
                return producto.getPrecioEspecial1Mayorista() != null
                        ? producto.getPrecioEspecial1Mayorista()
                        : producto.getPrecioBaseMayorista();

            case XXXXL:
                if (producto.getPrecioEspecial2Mayorista() != null) {
                    return producto.getPrecioEspecial2Mayorista();
                }
                if (producto.getPrecioEspecial1Mayorista() != null) {
                    return producto.getPrecioEspecial1Mayorista();
                }
                return producto.getPrecioBaseMayorista();

            case XXXXXL:
            case XXXXXXL:
                if (producto.getPrecioEspecial3Mayorista() != null) {
                    return producto.getPrecioEspecial3Mayorista();
                }
                if (producto.getPrecioEspecial2Mayorista() != null) {
                    return producto.getPrecioEspecial2Mayorista();
                }
                if (producto.getPrecioEspecial1Mayorista() != null) {
                    return producto.getPrecioEspecial1Mayorista();
                }
                return producto.getPrecioBaseMayorista();

            default:
                return producto.getPrecioBaseMayorista();
        }
    }

    @Transient
    public boolean usaPrecioHeredado() {

        if (this.talle == null || this.producto == null) {
            return false;
        }

        switch (this.talle) {

            case XXL:
            case XXXL:
                return producto.getPrecioEspecial1Publico() == null;

            case XXXXL:
                return producto.getPrecioEspecial2Publico() == null;

            case XXXXXL:
            case XXXXXXL:
                return producto.getPrecioEspecial3Publico() == null;

            default:
                return false;
        }
    }

    public void generarCodigoBarras() {
        if (this.producto != null && this.producto.getSku() != null
                && this.color != null && this.talle != null) {

            this.codigoBarrasReal = this.producto.getSku() + "-" +
                    this.color.name() + "-" +
                    this.talle.name();
        }
    }
}