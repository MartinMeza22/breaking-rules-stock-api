package com.breakingrules.stock.clientes.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import java.util.List;

public interface ClienteService {

    List<Cliente> listarTodos();

    Cliente obtenerPorId(Integer id);

    Cliente guardar(Cliente cliente);

    Cliente actualizar(Integer id, Cliente cliente);

    void eliminar(Integer id);
}