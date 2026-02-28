package com.breakingrules.stock.clientes.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Cliente actualizar(Integer id, Cliente cliente) {

        Cliente existente = obtenerPorId(id);

        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        existente.setDocumento(cliente.getDocumento());
        existente.getCuil();
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());
        existente.setDireccion(cliente.getDireccion());
        existente.setActivo(cliente.getActivo());

        return repository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}