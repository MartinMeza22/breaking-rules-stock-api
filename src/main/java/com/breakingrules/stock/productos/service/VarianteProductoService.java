package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.entity.Color;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.TipoTalle;
import com.breakingrules.stock.productos.entity.VarianteProducto;

import java.math.BigDecimal;
import java.util.List;

public interface VarianteProductoService {

    List<VarianteProducto> listarTodas();

    VarianteProducto obtenerPorId(Integer id);

    VarianteProducto crearVariante(
            Integer productoId,
            Talle talle,
            Color color,
            Integer stockInicial
    );

    void ingresarStock(Integer varianteId, Integer cantidad);

    void descontarStock(Integer varianteId, Integer cantidad);

    List<VarianteProducto> obtenerStockBajo(Integer limite);

    Integer obtenerStockTotal();

    void guardar(VarianteProducto variableProducto);

    void eliminar(Integer id);

    void validarDuplicado(Integer productoId, Color color, Talle talle, Integer idActual);

    List<Talle> obtenerTallesOcupados(Integer productoId, Color color, Integer varianteId);

    void sumarStock(Integer varianteId, Integer cantidad);

    List<Talle> obtenerTallesPorTipo(TipoTalle tipo);

    VarianteProducto obtenerPorCodigo(String codigo);
}