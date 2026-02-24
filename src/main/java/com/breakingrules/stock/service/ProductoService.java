package com.breakingrules.stock.service;

import com.breakingrules.stock.dto.ProductoDTO;
import com.breakingrules.stock.dto.ProductoStatsDTO;
import com.breakingrules.stock.model.Producto;
import com.breakingrules.stock.model.Talle;
import com.breakingrules.stock.repository.ProductoRepository;
import java.io.ByteArrayOutputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository repository;
    private static final int STOCK_BAJO_LIMITE = 5;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<ProductoDTO> listar() {
        log.info("Listando todos los productos");

        List<ProductoDTO> productos = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        log.info("Total productos obtenidos: {}", productos.size());
        return productos;
    }

    public Producto guardar(Producto producto) {
        log.info("Intentando guardar producto: {}", producto != null ? producto.getNombre() : "null");

        validarProducto(producto);

        Producto guardado = repository.save(producto);

        log.info("Producto guardado correctamente con ID: {}", guardado.getId());

        if (guardado.getStock() <= STOCK_BAJO_LIMITE) {
            log.warn("Producto con stock bajo guardado. ID: {}, Stock: {}", guardado.getId(), guardado.getStock());
        }

        return guardado;
    }

    public void eliminar(Integer id) {
        log.info("Intentando eliminar producto con ID: {}", id);

        if (id == null) {
            log.warn("Intento de eliminación con ID null");
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar producto inexistente. ID: {}", id);
            throw new IllegalArgumentException("El producto no existe");
        }

        repository.deleteById(id);
        log.info("Producto eliminado correctamente. ID: {}", id);
    }

    private void validarProducto(Producto producto) {

        if (producto == null) {
            log.warn("Validación fallida: producto null");
            throw new IllegalArgumentException("El producto no puede ser null");
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            log.warn("Validación fallida: nombre vacío");
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (producto.getCategoria() == null || producto.getCategoria().isBlank()) {
            log.warn("Validación fallida: categoría vacía");
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        if (producto.getPrecio() == null) {
            log.warn("Validación fallida: precio null");
            throw new IllegalArgumentException("El precio es obligatorio");
        }

        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Validación fallida: precio inválido {}", producto.getPrecio());
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() == null) {
            log.warn("Validación fallida: stock null");
            throw new IllegalArgumentException("El stock es obligatorio");
        }

        if (producto.getStock() < 0) {
            log.warn("Validación fallida: stock negativo {}", producto.getStock());
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    public List<ProductoDTO> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);

        if (nombre == null || nombre.isBlank()) {
            log.warn("Búsqueda fallida: nombre vacío");
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        List<ProductoDTO> resultados = repository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();

        log.info("Resultados encontrados: {}", resultados.size());
        return resultados;
    }

    public List<ProductoDTO> obtenerStockBajo() {
        log.info("Obteniendo productos con stock bajo (<= {})", STOCK_BAJO_LIMITE);

        List<ProductoDTO> productos = repository.findByStockGreaterThan(STOCK_BAJO_LIMITE)
                .stream()
                .map(this::toDTO)
                .toList();

        log.info("Productos con stock bajo encontrados: {}", productos.size());
        return productos;
    }

    public String exportarCSV() {
        log.info("Iniciando exportación de productos a CSV");

        try {
            List<Producto> productos = repository.findAll();
            log.info("Cantidad de productos a exportar: {}", productos.size());

            StringBuilder csv = new StringBuilder();
            csv.append("ID,Nombre,Categoria,Talle,Color,Precio,Stock\n");

            for (Producto p : productos) {
                csv.append(p.getId()).append(",")
                        .append(p.getNombre()).append(",")
                        .append(p.getCategoria()).append(",")
                        .append(p.getTalle()).append(",")
                        .append(p.getColor()).append(",")
                        .append(p.getPrecio()).append(",")
                        .append(p.getStock()).append("\n");
            }

            log.info("Exportación CSV completada correctamente");
            return csv.toString();

        } catch (Exception e) {
            log.error("Error al exportar productos a CSV", e);
            throw e;
        }
    }

    public byte[] exportarExcel() {
        log.info("Iniciando exportación de productos a Excel");

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            List<Producto> productos = repository.findAll();
            log.info("Cantidad de productos a exportar: {}", productos.size());

            Sheet sheet = workbook.createSheet("Productos");

            // ===== Header style =====
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            // ===== Header =====
            Row header = sheet.createRow(0);
            String[] columnas = {"ID", "Nombre", "Categoria", "Talle", "Color", "Precio", "Stock"};

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== Filas =====
            int rowIdx = 1;
            for (Producto p : productos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNombre());
                row.createCell(2).setCellValue(p.getCategoria());
                row.createCell(3).setCellValue(p.getTalle().name());
                row.createCell(4).setCellValue(p.getColor());
                row.createCell(5).setCellValue(p.getPrecio().doubleValue());
                row.createCell(6).setCellValue(p.getStock());
            }

            // Auto size columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            log.info("Exportación Excel completada correctamente");

            return out.toByteArray();

        } catch (Exception e) {
            log.error("Error al exportar productos a Excel", e);
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    public Page<ProductoDTO> listarPaginado(int page, int size) {

        log.info("Listando productos paginados - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Producto> productos = repository.findAll(pageable);

        log.info("Total elementos encontrados: {}", productos.getTotalElements());

        return productos.map(this::toDTO);
    }

    public Page<ProductoDTO> filtrar(
            String nombre,
            Talle talle,
            Integer stockMin,
            Integer stockMax,
            int page,
            int size
    ) {

        log.info("Filtrando productos - nombre: {}, talle: {}, stockMin: {}, stockMax: {}",
                nombre, talle, stockMin, stockMax);

        Pageable pageable = PageRequest.of(page, size);

        List<Producto> base = repository.findAll();

        Stream<Producto> stream = base.stream();

        if (nombre != null && !nombre.isBlank()) {
            stream = stream.filter(p ->
                    p.getNombre().toLowerCase().contains(nombre.toLowerCase()));
        }

        if (talle != null) {
            stream = stream.filter(p -> p.getTalle() == talle);
        }

        if (stockMin != null) {
            stream = stream.filter(p -> p.getStock() >= stockMin);
        }

        if (stockMax != null) {
            stream = stream.filter(p -> p.getStock() <= stockMax);
        }

        List<ProductoDTO> filtrados = stream
                .map(this::toDTO)
                .toList();

        int start = Math.min((int) pageable.getOffset(), filtrados.size());
        int end = Math.min(start + pageable.getPageSize(), filtrados.size());

        List<ProductoDTO> pageContent = filtrados.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filtrados.size());
    }

    public ProductoStatsDTO obtenerStats() {

        log.info("Calculando estadísticas de productos");

        List<Producto> productos = repository.findAll();

        long total = productos.size();

        int stockTotal = productos.stream()
                .mapToInt(Producto::getStock)
                .sum();

        BigDecimal precioPromedio = productos.stream()
                .map(Producto::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(total == 0 ? 1 : total), 2, RoundingMode.HALF_UP);

        long sinStock = productos.stream()
                .filter(p -> p.getStock() == 0)
                .count();

        return new ProductoStatsDTO(total, stockTotal, precioPromedio, sinStock);
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