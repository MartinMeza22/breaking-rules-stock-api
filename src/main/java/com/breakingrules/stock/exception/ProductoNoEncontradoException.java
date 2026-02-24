package com.breakingrules.stock.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(Long id) {
        super("No se encontró el producto con ID: " + id);
    }
}