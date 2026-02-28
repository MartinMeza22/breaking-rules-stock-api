package com.breakingrules.stock.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String documento;
    private String cuil;
    private String email;
    private String telefono;
    private String direccion;

    public ClienteDTO() {
    }

    private Boolean activo = true;

    public ClienteDTO(Integer id, Boolean activo, String direccion, String telefono, String email, String cuil, String documento, String apellido, String nombre) {
        this.id = id;
        this.activo = activo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.cuil = cuil;
        this.documento = documento;
        this.apellido = apellido;
        this.nombre = nombre;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
