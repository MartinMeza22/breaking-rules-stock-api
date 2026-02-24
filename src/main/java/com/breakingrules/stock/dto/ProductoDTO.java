package com.breakingrules.stock.dto;

import com.breakingrules.stock.model.Talle;

import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;
    private String nombre;
    private Talle talle;
    private String categoria;
    private String color;
    private BigDecimal precio;
    private Integer stock;

    public ProductoDTO() {}

    public ProductoDTO(Integer id, String nombre, String categoria, Talle talle,
                       String color, BigDecimal precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.talle = talle;
        this.color = color;
        this.precio = precio;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setCategoria(String marca) {
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}