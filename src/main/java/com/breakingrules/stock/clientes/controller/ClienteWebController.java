package com.breakingrules.stock.clientes.controller;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/clientes")
@RequiredArgsConstructor
public class ClienteWebController {

    private final ClienteService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listarTodos());
        model.addAttribute("clienteNuevo", new Cliente());
        return "clientes/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("clienteNuevo") Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("TIENE ERRORES");
            result.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            model.addAttribute("clientes", service.listarTodos());
            return "clientes/listar";
        }
        service.guardar(cliente);
        return "redirect:/web/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Cliente cliente = service.obtenerPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("cliente") Cliente cliente,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "clientes/editar";
        }

        service.actualizar(id, cliente);
        return "redirect:/web/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/clientes";
    }
}