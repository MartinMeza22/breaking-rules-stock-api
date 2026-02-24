package com.breakingrules.stock.dto;

import java.math.BigDecimal;

public class ProductoStatsDTO {
    private long totalProductos;
    private int stockTotal;
    private BigDecimal precioPromedio;
    private long productosSinStock;

    public ProductoStatsDTO(long totalProductos, int stockTotal, BigDecimal precioPromedio, long productosSinStock) {
        this.totalProductos = totalProductos;
        this.stockTotal = stockTotal;
        this.precioPromedio = precioPromedio;
        this.productosSinStock = productosSinStock;
    }
}
