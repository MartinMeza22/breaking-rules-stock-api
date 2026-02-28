package com.breakingrules.stock.proveedores.service;

import com.breakingrules.stock.proveedores.entity.Proveedor;

import java.util.List;

public interface ProveedorService {

    Proveedor guardar(Proveedor proveedor);

    Proveedor actualizar(Integer id, Proveedor proveedor);

    void eliminar(Integer id);

    Proveedor obtenerPorId(Integer id);

    List<Proveedor> listarTodos();
}