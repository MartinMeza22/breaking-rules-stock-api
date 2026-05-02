package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.caja.entity.MovimientoCaja;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VentaService {

    Venta crearVenta(Integer clienteId, String nombreCliente);

    void agregarProducto(Integer ventaId, Integer varianteId, Integer cantidad);

    void finalizarVenta(Integer ventaId, BigDecimal descuento);

    Venta obtenerVenta(Integer ventaId);

    List<VentaDetalle> obtenerDetalles(Integer ventaId);

    List<Venta> listarVentas();

     void eliminarProducto(Integer detalleId);

    void reabrirVenta(Integer ventaId);

    void anularVenta(Integer ventaId);

    void cancelarSiEstaVacia(Integer ventaId);

    Map<String, Object> agregarProductoPorCodigo(Integer ventaId, String codigoBarras);

    void actualizarCantidad(Integer detalleId, Integer cantidad);

    Map<String, Object> buscarProductoPorCodigo(Integer ventaId, String codigo);
}