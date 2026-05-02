package com.breakingrules.stock.productos.controller;

import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.TipoTalle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.service.EtiquetaService;
import com.breakingrules.stock.productos.service.ProductoService;
import com.breakingrules.stock.productos.service.ProductoServiceImpl;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import com.breakingrules.stock.proveedores.service.ProveedorService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequestMapping("/web/productos")
@RequiredArgsConstructor
public class ProductoWebController {

    private final ProductoService service;
    private final ProveedorService proveedorService;
    private final VarianteProductoService varianteProductoService;
    private final EtiquetaService etiquetaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", service.listarTodosSinPaginacion());
        model.addAttribute("productoNuevo", new Producto());
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "productos/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("productoNuevo") Producto producto,
                          BindingResult result,
                          @RequestParam(required = false) Integer proveedorId,
                          Model model) {

        if(service.existeSku(producto.getSku())){
            result.rejectValue("sku", "error.producto", "Ya existe un producto con ese articulo");
        }

        if (result.hasErrors()) {
            model.addAttribute("productos", service.listarTodosSinPaginacion());
            model.addAttribute("proveedores", proveedorService.listarTodos());
            model.addAttribute("productoNuevo", producto);
            return "productos/listar";
        }

        if (proveedorId != null) {
            producto.setProveedor(proveedorService.obtenerPorId(proveedorId));
        }

        if (producto.getTipoTalle() == null) {
            producto.setTipoTalle(TipoTalle.ALFABETICO);
        }

        service.guardar(producto);
        return "redirect:/web/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto producto = service.obtenerEntidadPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "productos/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("producto") Producto producto,
                             BindingResult result,
                             @RequestParam(required = false) Integer proveedorId,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("proveedores", proveedorService.listarTodos());
            return "productos/editar";
        }

        Producto existente = service.obtenerEntidadPorId(id);
        existente.setNombre(producto.getNombre());
        existente.setSku(producto.getSku());
        existente.setCosto(producto.getCosto());
        existente.setTipoTalle(producto.getTipoTalle());
        existente.setPrecioBasePublico(producto.getPrecioBasePublico());
        existente.setPrecioBaseMayorista(producto.getPrecioBaseMayorista());
        existente.setPrecioEspecial1Publico(producto.getPrecioEspecial1Publico());
        existente.setPrecioEspecial1Mayorista(producto.getPrecioEspecial1Mayorista());
        existente.setPrecioEspecial2Publico(producto.getPrecioEspecial2Publico());
        existente.setPrecioEspecial2Mayorista(producto.getPrecioEspecial2Mayorista());
        existente.setPrecioEspecial3Publico(producto.getPrecioEspecial3Publico());
        existente.setPrecioEspecial3Mayorista(producto.getPrecioEspecial3Mayorista());
        if (existente.getVariantes() != null) {
            existente.getVariantes().forEach(v -> v.generarCodigoBarras());
        }
        if (proveedorId != null) {
            existente.setProveedor(proveedorService.obtenerPorId(proveedorId));
        } else {
            existente.setProveedor(null);
        }

        service.guardar(existente);
        return "redirect:/web/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/productos";
    }

    @GetMapping(value = "/barcode/{codigo}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] generarCodigoBarras(@PathVariable String codigo) throws Exception {
        Code128Writer writer = new Code128Writer();
        BitMatrix bitMatrix = writer.encode(codigo, BarcodeFormat.CODE_128, 300, 100);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @PostMapping("/guardar/{productoId}")
    public String guardarVariante(@PathVariable Integer productoId,
                                  @ModelAttribute VarianteProducto varianteNueva) {

        varianteProductoService.crearVariante(
                productoId,
                varianteNueva.getTalle(),
                varianteNueva.getColor(),
                varianteNueva.getStock()
        );

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/etiqueta/{id}")
    public ResponseEntity<byte[]> imprimirEtiqueta(@PathVariable Integer id) throws Exception {
        VarianteProducto variante = varianteProductoService.obtenerPorId(id);
        List<VarianteProducto> lista = List.of(variante);
        byte[] pdf = etiquetaService.generarEtiquetas(lista);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=etiqueta.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}