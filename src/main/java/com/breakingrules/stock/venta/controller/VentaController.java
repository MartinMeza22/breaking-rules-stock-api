package com.breakingrules.stock.venta.controller;

import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.venta.dto.ItemVentaDTO;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.venta.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @GetMapping
    public String redirectToNueva() {
        return "redirect:/web/ventas/nueva";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        VentaDTO venta = new VentaDTO();
        venta.getItems().add(new ItemVentaDTO());
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        return "ventas/nueva";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute VentaDTO ventaDTO, RedirectAttributes redirectAttributes, Model model) {
        try {
            ventaService.confirmarVenta(ventaDTO);
            redirectAttributes.addFlashAttribute("success", "Venta registrada correctamente");
            return "redirect:/web/ventas/nueva";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("productos", productoRepository.findAll());
            return "ventas/nueva";
        }
    }
}