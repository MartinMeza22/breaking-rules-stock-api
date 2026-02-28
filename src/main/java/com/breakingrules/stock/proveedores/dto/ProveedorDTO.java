package com.breakingrules.stock.proveedores.dto;

import com.breakingrules.stock.productos.entity.Talle;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProveedorDTO {

    private Integer id;
    private String nombre;
    private String cuit;
    private String email;
    private String telefono;
    private String direccion;
    private Boolean activo;

    public ProveedorDTO() {
    }

    public ProveedorDTO(Integer id, String nombre, String cuit, String email, String telefono, String direccion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.cuit = cuit;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}

