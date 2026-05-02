package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.dto.ProductoStatsDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.TipoTalle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.*;
@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);
    private final ProductoRepository repository;
    private static final int STOCK_BAJO_LIMITE = 5;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductoDTO> listarTodosSinPaginacion() {
        log.info("Listando productos activos");
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Producto guardar(Producto producto) {
        log.info("Intentando guardar producto: {}", producto != null ? producto.getSku() : "null");

        prepararYValidar(producto);

        Producto guardado = repository.save(producto);
        log.info("Producto guardado correctamente con ID: {}", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Producto actualizar(Producto producto) {
        log.info("Actualizando producto ID: {}", producto.getId());

        Producto existente = repository.findById(producto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        existente.setSku(producto.getSku());
        existente.setNombre(producto.getNombre());
        existente.setCosto(producto.getCosto());
        existente.setPrecioBasePublico(producto.getPrecioBasePublico());
        existente.setPrecioBaseMayorista(producto.getPrecioBaseMayorista());

        existente.setPrecioEspecial1Publico(producto.getPrecioEspecial1Publico());
        existente.setPrecioEspecial1Mayorista(producto.getPrecioEspecial1Mayorista());
        existente.setPrecioEspecial2Publico(producto.getPrecioEspecial2Publico());
        existente.setPrecioEspecial2Mayorista(producto.getPrecioEspecial2Mayorista());
        existente.setPrecioEspecial3Publico(producto.getPrecioEspecial3Publico());
        existente.setPrecioEspecial3Mayorista(producto.getPrecioEspecial3Mayorista());

        existente.setProveedor(producto.getProveedor());
        existente.setTipoTalle(producto.getTipoTalle());

        if (existente.getVariantes() != null) {
            log.info("Regenerando códigos para {} variantes", existente.getVariantes().size());
            for (VarianteProducto v : existente.getVariantes()) {
                v.generarCodigoBarras();
            }
        }

        prepararYValidar(existente);

        return repository.save(existente);
    }

    private void prepararYValidar(Producto producto) {
        if (producto.getTipoTalle() == null) {
            producto.setTipoTalle(TipoTalle.ALFABETICO);
        }

        if (producto.getTipoTalle() == TipoTalle.NUMERICO) {
            limpiarPreciosEspeciales(producto);
        }

        validarProducto(producto);
    }

    private void limpiarPreciosEspeciales(Producto producto) {
        log.info("Limpiando precios especiales para producto numérico");
        producto.setPrecioEspecial1Publico(null);
        producto.setPrecioEspecial1Mayorista(null);
        producto.setPrecioEspecial2Publico(null);
        producto.setPrecioEspecial2Mayorista(null);
        producto.setPrecioEspecial3Publico(null);
        producto.setPrecioEspecial3Mayorista(null);
    }

    private void validarProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser null");

        if (producto.getPrecioBasePublico() == null || producto.getPrecioBasePublico().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio público base debe ser mayor a 0");
        }

        if (producto.getPrecioBaseMayorista() == null || producto.getPrecioBaseMayorista().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio mayorista base debe ser mayor a 0");
        }

        if (producto.getTipoTalle() == TipoTalle.ALFABETICO) {
            validarPreciosEspeciales(producto);
        }

        if (producto.getActivo() == null) producto.setActivo(true);
    }

    private void validarPreciosEspeciales(Producto producto) {
        if (producto.getPrecioEspecial1Publico() != null && producto.getPrecioEspecial1Mayorista() != null) {
            if (producto.getPrecioEspecial1Publico().compareTo(producto.getPrecioEspecial1Mayorista()) < 0) {
                throw new IllegalArgumentException("El precio público 2XL/3XL no puede ser menor al mayorista");
            }
        }
        // Se pueden agregar más validaciones para Especial 2 y 3 aquí
    }

    @Override
    public void eliminar(Integer id) {
        log.info("Desactivando producto ID: {}", id);
        Producto producto = obtenerEntidadPorId(id);
        producto.setActivo(false);
        if (producto.getVariantes() != null) {
            producto.getVariantes().forEach(v -> v.setActivo(false));
        }
        repository.save(producto);
    }

    @Override
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        return repository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Producto obtenerEntidadPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Page<ProductoDTO> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(this::toDTO);
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(
                p.getId(), p.getSku(), p.getNombre(), p.getCosto(),
                p.getPrecioBasePublico(), p.getPrecioBaseMayorista(),
                p.getPrecioEspecial1Publico(), p.getPrecioEspecial2Publico(), p.getPrecioEspecial3Publico(),
                p.getPrecioEspecial1Mayorista(), p.getPrecioEspecial2Mayorista(), p.getPrecioEspecial3Mayorista(), p.getTipoTalle(),
                p.getActivo(), p.getStockTotal(),
                p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor"
        );
    }

    @Override
    public boolean existeSku(String sku) {
        return repository.existsBySku(sku);
    }

    @Override
    public List<VarianteProducto> obtenerVariantesOrdenadas(Integer productoId) {
        Producto producto = obtenerEntidadPorId(productoId);
        List<VarianteProducto> variantes = new ArrayList<>(producto.getVariantes());
        variantes.sort(Comparator
                .comparing((VarianteProducto v) -> v.getColor().ordinal())
                .thenComparing(v -> v.getTalle().ordinal()));
        return variantes;
    }

    @Override
    public String exportarCSV() {
        List<Producto> productos = repository.findAll();
        StringBuilder csv = new StringBuilder("ID,SKU,Nombre,Costo,PrecioBasePublico,PrecioBaseMayorista,Activo\n");
        for (Producto p : productos) {
            csv.append(p.getId()).append(",")
                    .append(p.getSku()).append(",")
                    .append(p.getNombre()).append(",")
                    .append(p.getCosto()).append(",")
                    .append(p.getPrecioBasePublico()).append(",")
                    .append(p.getPrecioBaseMayorista()).append(",")
                    .append(p.getActivo()).append("\n");
        }
        return csv.toString();
    }

    @Override
    public Integer obtenerStockTotal(Integer productoId) {
        Producto producto = obtenerEntidadPorId(productoId);
            return producto.getVariantes() .stream() .mapToInt(v -> v.getStock() != null ? v.getStock() : 0) .sum();
    }
}