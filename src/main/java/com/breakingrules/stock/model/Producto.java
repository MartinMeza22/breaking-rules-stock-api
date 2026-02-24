package com.breakingrules.stock.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String categoria;
    @Enumerated(EnumType.STRING)
    private Talle talle;
    private String color;
    private BigDecimal precio;
    private Integer stock;

    public Producto(){ }

    public Producto(Integer id, String nombre, String categoria, Talle talle, String color, BigDecimal precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.talle = talle;
        this.color = color;
        this.precio = precio;
        this.stock = stock;
    }


}