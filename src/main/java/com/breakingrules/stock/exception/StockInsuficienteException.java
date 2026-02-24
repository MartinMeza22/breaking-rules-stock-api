package com.breakingrules.stock.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(Long id) {
        super("Stock insuficiente para el producto con ID: " + id);
    }
}