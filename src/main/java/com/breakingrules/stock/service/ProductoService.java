package com.breakingrules.stock.service;

import com.breakingrules.stock.dto.ProductoDTO;
import com.breakingrules.stock.model.Producto;
import com.breakingrules.stock.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private static final int STOCK_BAJO_LIMITE = 5;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<ProductoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Producto guardar(Producto producto) {
        validarProducto(producto);
        return repository.save(producto);
    }

    public void eliminar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El producto no existe");
        }

        repository.deleteById(id);
    }

    private void validarProducto(Producto producto) {

        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (producto.getCategoria() == null || producto.getCategoria().isBlank()) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        if (producto.getPrecio() == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }

        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() == null) {
            throw new IllegalArgumentException("El stock es obligatorio");
        }

        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    public List<ProductoDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        return repository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();
    }

        public List<ProductoDTO> obtenerStockBajo() {
            return repository.findByStockGreaterThan(STOCK_BAJO_LIMITE)
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }

    public String exportarCSV() {
        List<Producto> productos = repository.findAll();

        StringBuilder csv = new StringBuilder();

        // Cabecera
        csv.append("ID,Nombre,Categoria,Talle,Color,Precio,Stock\n");

        // Filas
        for (Producto p : productos) {
            csv.append(p.getId()).append(",")
                    .append(p.getNombre()).append(",")
                    .append(p.getCategoria()).append(",")
                    .append(p.getTalle()).append(",")
                    .append(p.getColor()).append(",")
                    .append(p.getPrecio()).append(",")
                    .append(p.getStock()).append("\n");
        }

        return csv.toString();
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                p.getTalle(),
                p.getColor(),
                p.getPrecio(),
                p.getStock()
        );
    }
}