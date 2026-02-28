package com.breakingrules.stock.productos.controller;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.dto.ProductoStatsDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.service.ProductoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "Productos", description = "Operaciones relacionadas a productos de indumentaria")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoServiceImpl service;

    public ProductoController(ProductoServiceImpl service) {
        this.service = service;
    }

    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados en el sistema")
    @GetMapping
    public Page<ProductoDTO> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.listarPaginado(page, size);
    }

    @Operation(summary = "Buscar productos por nombre", description = "Filtra productos que contengan el texto indicado en el nombre")
    @GetMapping("/buscar")
    public Page<ProductoDTO> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Talle talle,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false) Integer stockMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.filtrar(nombre, talle, stockMin, stockMax, page, size);
    }
    @Operation(summary = "Productos con stock bajo", description = "Devuelve productos cuyo stock es menor o igual al mínimo indicado")
    @GetMapping("/stock-bajo")
    public List<ProductoDTO> stockBajo() {
        return service.obtenerStockBajo();
    }

    @Operation(summary = "Exportar SCV - Excel", description = "Descarga de archivo SCV con todos los articulos - Excel")
    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarCSV() {

        String csv = service.exportarCSV();
        byte[] bom = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
        byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);

        byte[] contenido = new byte[bom.length + csvBytes.length];
        System.arraycopy(bom, 0, contenido, 0, bom.length);
        System.arraycopy(csvBytes, 0, contenido, bom.length, csvBytes.length);
        // Nombre dinámico
        String fecha = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String nombreArchivo = "productos-" + fecha + ".csv";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + nombreArchivo)
                .header("Content-Type", "text/csv; charset=UTF-8")
                .body(contenido);
    }

//    @Operation(summary = "Exportar productos a Excel", description = "Descarga archivo XLSX con todos los productos")
//    @GetMapping("/exportar/excel")
//    public ResponseEntity<byte[]> exportarExcel() {
//
//        byte[] excel = service.exportarExcel();
//
//        String fecha = LocalDate.now()
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//        String nombreArchivo = "productos-" + fecha + ".xlsx";
//
//        return ResponseEntity.ok()
//                .header("Content-Disposition", "attachment; filename=" + nombreArchivo)
//                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//                .body(excel);
//    }

    @GetMapping("/stats")
    public ProductoStatsDTO stats() {
        return service.obtenerStats();
    }
}