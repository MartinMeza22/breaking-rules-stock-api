package com.breakingrules.stock.venta.dto;

import java.math.BigDecimal;

public class ItemVentaDTO {
    private Integer productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String observaciones;
    private BigDecimal subtotal;

    public ItemVentaDTO() {
    }

    public ItemVentaDTO(Integer productoId, String observaciones, BigDecimal precioUnitario, Integer cantidad, BigDecimal subtotal) {
        this.productoId = productoId;
        this.observaciones = observaciones;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
