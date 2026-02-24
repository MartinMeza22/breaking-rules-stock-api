package com.breakingrules.stock.dto;

import com.breakingrules.stock.model.Talle;

public class ProductoFiltroDTO {
    private String nombre;
    private Talle talle;
    private Integer stockMin;
    private Integer stockMax;

    public ProductoFiltroDTO(String nombre, Integer stockMax, Integer stockMin, Talle talle) {
        this.nombre = nombre;
        this.stockMax = stockMax;
        this.stockMin = stockMin;
        this.talle = talle;
    }
}