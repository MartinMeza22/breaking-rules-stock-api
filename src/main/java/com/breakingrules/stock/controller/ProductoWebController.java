package com.breakingrules.stock.controller;

import com.breakingrules.stock.dto.ProductoDTO;
import com.breakingrules.stock.model.Producto;
import com.breakingrules.stock.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/productos")
public class ProductoWebController {

    private final ProductoService service;

    public ProductoWebController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", service.listar());
        model.addAttribute("productoNuevo", new Producto());
        return "productos";
    }

    @PostMapping
    public String crear(@ModelAttribute Producto producto) {
        service.guardar(producto);
        return "redirect:/web/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/productos";
    }

}