package com.breakingrules.stock.venta.controller;

import com.breakingrules.stock.clientes.service.ClienteService;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;
import com.breakingrules.stock.venta.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/ventas")
public class VentaWebController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final VarianteProductoService varianteProductoService;

    public VentaWebController(
            VentaService ventaService,
            ClienteService clienteService,
            VarianteProductoService varianteProductoService
    ) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.varianteProductoService = varianteProductoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientePublico", clienteService.obtenerClientePublico());
        model.addAttribute("mayoristas", clienteService.obtenerMayoristas());
        return "ventas/crear";
    }

    @PostMapping("/crear")
    public String crearVenta(
            @RequestParam Integer clienteId,
            @RequestParam(required = false) String nombreCliente
    ) {

        Venta venta = ventaService.crearVenta(clienteId, nombreCliente);

        return "redirect:/web/ventas/" + venta.getId();
    }


    @GetMapping("/{ventaId}")
    public String verVenta(@PathVariable Integer ventaId, Model model) {

        model.addAttribute("venta", ventaService.obtenerVenta(ventaId));
        model.addAttribute("detalles", ventaService.obtenerDetalles(ventaId));
        model.addAttribute("variantes", varianteProductoService.listarTodas());

        return "ventas/detalle";
    }

    @PostMapping("/agregar-producto")
    public String agregarProducto(
            @RequestParam Integer ventaId,
            @RequestParam(required = false) Integer varianteId,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Integer cantidad,
            RedirectAttributes redirectAttributes
    ) {

        try {

            //  SCANNER
            if (codigo != null && !codigo.isBlank()) {

                ventaService.agregarProductoPorCodigo(ventaId, codigo);
                return "redirect:/web/ventas/" + ventaId;
            }

            //  MANUAL
            if (varianteId == null) {
                redirectAttributes.addFlashAttribute("error", "Seleccioná un producto");
                return "redirect:/web/ventas/" + ventaId;
            }

            if (cantidad == null || cantidad <= 0) {
                redirectAttributes.addFlashAttribute("error", "Ingresá una cantidad válida");
                return "redirect:/web/ventas/" + ventaId;
            }

            ventaService.agregarProducto(ventaId, varianteId, cantidad);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo agregar el producto");
        }

        return "redirect:/web/ventas/" + ventaId;
    }

    @PostMapping("/finalizar")
    public String finalizarVenta(
            @RequestParam Integer ventaId,
            @RequestParam(required = false) BigDecimal descuento,
            Model model
    ) {

        try {
            ventaService.finalizarVenta(ventaId, descuento);
            return "redirect:/web/ventas/historial";
        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());

            model.addAttribute("venta", ventaService.obtenerVenta(ventaId));
            model.addAttribute("detalles", ventaService.obtenerDetalles(ventaId));
            model.addAttribute("variantes", varianteProductoService.listarTodas()
                    .stream()
                    .map(v -> Map.of(
                            "id", v.getId(),
                            "nombre", v.getProducto().getNombre(),
                            "color", v.getColor(),
                            "talle", v.getTalle(),
                            "stock", v.getStock()
                    ))
                    .toList());

            return "ventas/detalle";
        }
    }

    @GetMapping("/historial")
    public String historial(Model model) {

        model.addAttribute("ventas", ventaService.listarVentas());

        return "ventas/historial";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleVenta(@PathVariable Integer id, Model model) {

        Venta venta = ventaService.obtenerVenta(id);
        List<VentaDetalle> detalles = ventaService.obtenerDetalles(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);

        return "ventas/detalleVenta";
    }

    @GetMapping("/remito/{id}")
    public String generarRemito(@PathVariable Integer id, Model model) {
        Venta venta = ventaService.obtenerVenta(id); List<VentaDetalle> detalles = ventaService.obtenerDetalles(id);
        BigDecimal subtotal = detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        return "ventas/remito"; }

    @PostMapping("/eliminar-producto")
    public String eliminarProducto(
            @RequestParam Integer ventaId,
            @RequestParam Integer detalleId
    ) {
        ventaService.eliminarProducto(detalleId);
        return "redirect:/web/ventas/" + ventaId;
    }

    @PostMapping("/reabrir")
    public String reabrirVenta(@RequestParam Integer ventaId) {

        ventaService.reabrirVenta(ventaId);

        return "redirect:/web/ventas/" + ventaId;
    }

    @PostMapping("/anular")
    public String anularVenta(@RequestParam Integer ventaId) {

        ventaService.anularVenta(ventaId);

        return "redirect:/web/ventas/historial";
    }

    @PostMapping("/cancelar")
    @ResponseStatus(HttpStatus.OK) //
    public void cancelarVenta(@RequestParam Integer ventaId) {
        ventaService.cancelarSiEstaVacia(ventaId);
    }


    @PostMapping("/scan")
    @ResponseBody
    public Map<String, Object> scan(
            @RequestParam Integer ventaId,
            @RequestParam String codigo
    ) {
        return ventaService.buscarProductoPorCodigo(ventaId, codigo);
    }

    @PostMapping("/actualizar-cantidad")
    public String actualizarCantidad(
            @RequestParam Integer ventaId,
            @RequestParam Integer detalleId,
            @RequestParam Integer cantidad
    ) {

        ventaService.actualizarCantidad(detalleId, cantidad);

        return "redirect:/web/ventas/" + ventaId;
    }
}