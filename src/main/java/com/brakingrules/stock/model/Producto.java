package com.brakingrules.stock.model;

import jakarta.persistence.*;
import lombok.*;

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
    private String talle;
    private String color;
    private Double precio;
    private Integer stock;

    public Producto(){ }

    public Producto(Integer id, String nombre, String categoria, String talle, String color, Double precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.talle = talle;
        this.color = color;
        this.precio = precio;
        this.stock = stock;
    }


}