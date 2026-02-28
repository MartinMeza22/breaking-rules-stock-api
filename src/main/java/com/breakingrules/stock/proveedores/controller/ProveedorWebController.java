package com.breakingrules.stock.proveedores.controller;

import com.breakingrules.stock.proveedores.entity.Proveedor;
import com.breakingrules.stock.proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "proveedores/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proveedorNuevo") Proveedor proveedor,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("proveedores", service.listarTodos());
            return "proveedores/listar";
        }

        service.guardar(proveedor);
        return "redirect:/web/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Proveedor proveedor = service.obtenerPorId(id);
        model.addAttribute("proveedor", proveedor);
        return "proveedores/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("proveedor") Proveedor proveedor,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            return "proveedores/editar";
        }

        service.actualizar(id, proveedor);
        return "redirect:/web/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/proveedores";
    }
}