package com.breakingrules.stock.productos.controller;

import com.breakingrules.stock.productos.entity.*;
import com.breakingrules.stock.productos.service.ProductoService;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/variantes")
@RequiredArgsConstructor
public class VarianteProductoWebController {

    private final VarianteProductoService varianteService;
    private final ProductoService productoService;

    @PostMapping("/crear")
    public String crearVariante(
            @RequestParam Integer productoId,
            @RequestParam Color color,
            @RequestParam Talle talle,
            @RequestParam Integer stockInicial
    ) {

        varianteService.crearVariante(
                productoId,
                talle,
                color,
                stockInicial
        );

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/{productoId}")
    public String listar(@PathVariable Integer productoId, Model model) {

        Producto producto = productoService.obtenerEntidadPorId(productoId);

        model.addAttribute("producto", producto);
        model.addAttribute("variantes", productoService.obtenerVariantesOrdenadas(productoId));
        model.addAttribute("colores", Color.values());

        List<Talle> tallesFiltrados;

        if (producto.getTipoTalle() != null &&
                producto.getTipoTalle().name().equals("ALFABETICO")) {

            tallesFiltrados = List.of(
                    Talle.S, Talle.M, Talle.L, Talle.XL,
                    Talle.XXL, Talle.XXXL, Talle.XXXXL, Talle.XXXXXL, Talle.XXXXXXL
            );

        } else {

            tallesFiltrados = List.of(
                    Talle.T38, Talle.T40, Talle.T42, Talle.T44,
                    Talle.T46, Talle.T48, Talle.T50,
                    Talle.T52
            );
        }

        model.addAttribute("talles", tallesFiltrados);

        model.addAttribute("varianteNueva", new VarianteProducto());
        model.addAttribute("stockTotal", productoService.obtenerStockTotal(productoId));

        return "variantes/listar";
    }
    @PostMapping("/guardar/{productoId}")
    public String guardar(@PathVariable Integer productoId,
                          @ModelAttribute VarianteProducto variante,
                          RedirectAttributes redirectAttributes) {

        Producto producto = productoService.obtenerEntidadPorId(productoId);

        variante.setProducto(producto);

        try {
            varianteService.guardar(variante);
        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/web/variantes/" + productoId;
        }

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/eliminar/{varianteId}/{productoId}")
    public String eliminar(@PathVariable Integer varianteId,
                           @PathVariable Integer productoId) {

        varianteService.eliminar(varianteId);

        return "redirect:/web/variantes/" + productoId;
    }

    @PostMapping("/ingresar-stock")
    public String ingresarStock(
            @RequestParam Integer varianteId,
            @RequestParam(required = false) Integer cantidad,
            RedirectAttributes redirectAttributes
    ) {

        if (cantidad == null || cantidad == 0) {

            redirectAttributes.addFlashAttribute("error", "Ingresá una cantidad válida (distinta de cero)");
            redirectAttributes.addFlashAttribute("errorVarianteId", varianteId);

            Integer productoId = varianteService
                    .obtenerPorId(varianteId)
                    .getProducto()
                    .getId();

            return "redirect:/web/variantes/" + productoId;
        }

        varianteService.ingresarStock(varianteId, cantidad);

        Integer productoId = varianteService
                .obtenerPorId(varianteId)
                .getProducto()
                .getId();

        return "redirect:/web/variantes/" + productoId;
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        VarianteProducto variante = varianteService.obtenerPorId(id);

        List<Talle> tallesBloqueados = variante.getProducto().getVariantes().stream()
                .filter(v -> v.getColor().equals(variante.getColor()))
                .filter(v -> !v.getId().equals(variante.getId()))
                .map(VarianteProducto::getTalle)
                .toList();

        List<Talle> tallesFiltrados;

        if (variante.getProducto().getTipoTalle() == TipoTalle.ALFABETICO) {

            tallesFiltrados = List.of(
                    Talle.S, Talle.M, Talle.L, Talle.XL,
                    Talle.XXL, Talle.XXXL, Talle.XXXXL, Talle.XXXXXL, Talle.XXXXXXL
            );

        } else {

            tallesFiltrados = List.of(
                    Talle.T38, Talle.T40, Talle.T42, Talle.T44,
                    Talle.T46, Talle.T48, Talle.T50,
                    Talle.T52
            );
        }

        model.addAttribute("variante", variante);
        model.addAttribute("colores", Color.values());
        model.addAttribute("talles", tallesFiltrados);
        model.addAttribute("tallesBloqueados", tallesBloqueados);

        return "variantes/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @ModelAttribute VarianteProducto variante,
                             RedirectAttributes redirectAttributes) {

        VarianteProducto existente = varianteService.obtenerPorId(id);

        existente.setColor(variante.getColor());
        existente.setTalle(variante.getTalle());

        Integer productoId = existente.getProducto().getId();

        try {
            varianteService.guardar(existente);
        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/web/variantes/editar/" + id;
        }

        return "redirect:/web/variantes/" + productoId;
    }


    @PostMapping("/guardar-multiple/{productoId}")
    public String guardarMultiples(
            @PathVariable Integer productoId,
            @RequestParam Color color,
            @RequestParam(required = false) List<Talle> tallesSeleccionados,
            @RequestParam Map<String, String> stock
    ) {

        if (tallesSeleccionados != null) {

            for (Talle talle : tallesSeleccionados) {

                String valor = stock.get("stock[" + talle.name() + "]");

                Integer cantidad = 0;

                if (valor != null && !valor.isBlank()) {
                    cantidad = Integer.parseInt(valor);
                }

                varianteService.crearVariante(
                        productoId,
                        talle,
                        color,
                        cantidad
                );
            }
        }

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/talles-ocupados")
    @ResponseBody
    public List<Talle> obtenerTallesOcupados(
            @RequestParam Integer productoId,
            @RequestParam Color color,
            @RequestParam Integer varianteId
    ) {

        return varianteService.obtenerTallesOcupados(productoId, color, varianteId);
    }
}