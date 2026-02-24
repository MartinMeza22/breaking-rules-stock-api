package com.breakingrules.stock.controller;

import com.breakingrules.stock.dto.ProductoDTO;
import com.breakingrules.stock.model.Producto;
import com.breakingrules.stock.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos", description = "Operaciones relacionadas a productos de indumentaria")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados en el sistema")
    @GetMapping
    public List<ProductoDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Crear producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return service.guardar(producto);
    }

    @Operation(summary = "Buscar productos por nombre", description = "Filtra productos que contengan el texto indicado en el nombre")
    @GetMapping("/buscar")
    public List<ProductoDTO> buscar(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
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
}