package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.dto.ProductoStatsDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> listarTodosSinPaginacion();

    Producto guardar(Producto producto);

    Producto actualizar(Producto producto);

    void eliminar(Integer id);

    List<ProductoDTO> buscarPorNombre(String nombre);

    ProductoDTO buscarPorId(Integer id);

    Producto obtenerEntidadPorId(Integer id);

    List<ProductoDTO> obtenerStockBajo();

    Page<ProductoDTO> listarPaginado(int page, int size);

    public Page<ProductoDTO> filtrar( String nombre, Talle talle, Integer stockMin, Integer stockMax, int page, int size);

    ProductoStatsDTO obtenerStats();


    void generarImagenCodigoBarras(String codigo, String sku);

}
