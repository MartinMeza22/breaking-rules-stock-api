package com.breakingrules.stock.proveedores.service;

import com.breakingrules.stock.proveedores.entity.Proveedor;
import com.breakingrules.stock.proveedores.repository.ProveedorRepository;
import com.breakingrules.stock.proveedores.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repository;

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return repository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Integer id, Proveedor proveedor) {
        Proveedor existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        existente.setNombre(proveedor.getNombre());
        existente.setCuit(proveedor.getCuit());
        existente.setEmail(proveedor.getEmail());
        existente.setTelefono(proveedor.getTelefono());
        existente.setDireccion(proveedor.getDireccion());
        existente.setActivo(proveedor.getActivo());
        return repository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Proveedor obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    @Override
    public List<Proveedor> listarTodos() {
        return repository.findAll();
    }
}