package com.breakingrules.stock.productos.controller;

import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.service.ProductoServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/productos")
@RequiredArgsConstructor
public class ProductoWebController {

    private final ProductoServiceImpl service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", service.listarTodosSinPaginacion());
        model.addAttribute("productoNuevo", new Producto());
        model.addAttribute("talles", Talle.values());
        return "productos/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("productoNuevo") Producto producto,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("productos", service.listarTodosSinPaginacion());
            model.addAttribute("talles", Talle.values());
            return "productos/listar"; // vuelve al form con errores
        }

        service.guardar(producto);
        return "redirect:/web/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto producto = service.obtenerEntidadPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("talles", Talle.values());
        return "productos/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("producto") Producto producto,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("talles", Talle.values());
            return "productos/editar"; // volver al form de edición
        }

        Producto existente = service.obtenerEntidadPorId(id);

        existente.setNombre(producto.getNombre());
        existente.setSku(existente.getSku());
        existente.setCodigoBarras(existente.getCodigoBarras());
        existente.setPrecioVenta(producto.getPrecioVenta());
        existente.setStock(producto.getStock());
        existente.setCategoria(producto.getCategoria());
        existente.setColor(producto.getColor());

        service.actualizar(existente);

        return "redirect:/web/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/productos";
    }

}