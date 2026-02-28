package com.breakingrules.stock.productos.dto;

import com.breakingrules.stock.productos.entity.Talle;

import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;
    private String sku;
    private String nombre;
    private String categoria;
    private Talle talle;
    private String color;

    private String codigoBarras;

    private BigDecimal costo;
    private BigDecimal precioVenta;

    private Integer stock;
    private Integer stockMinimo;

    private Boolean activo;
    private String proveedorNombre;
    public ProductoDTO() {}

    public ProductoDTO(Integer id, String sku, String nombre, String categoria, Talle talle,
                       String color, String codigoBarras,
                       BigDecimal costo, BigDecimal precioVenta,
                       Integer stock, Integer stockMinimo, Boolean activo, String proveedorNombre) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.categoria = categoria;
        this.talle = talle;
        this.color = color;
        this.codigoBarras = codigoBarras;
        this.costo = costo;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
        this.proveedorNombre = proveedorNombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Talle getTalle() {
        return talle;
    }

    public void setTalle(Talle talle) {
        this.talle = talle;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
}