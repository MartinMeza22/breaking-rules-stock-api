package com.breakingrules.stock.proveedores.controller;

import com.breakingrules.stock.proveedores.entity.Proveedor;
import com.breakingrules.stock.proveedores.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/proveedores")
@RequiredArgsConstructor
public class ProveedorWebController {

    private final ProveedorService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", service.listarTodos());
        model.addAttribute("proveedorNuevo", new Proveedor());
        return "proveedores/listar"; // Thymeleaf view
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proveedor proveedor) {
        service.guardar(proveedor);
        return "redirect:/web/proveedores/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Proveedor proveedor = service.obtenerPorId(id);
        model.addAttribute("proveedor", proveedor);
        return "proveedores/editar"; // Thymeleaf view
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute Proveedor proveedor) {
        service.actualizar(id, proveedor);
        return "redirect:/web/proveedores/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/proveedores/listar";
    }
}