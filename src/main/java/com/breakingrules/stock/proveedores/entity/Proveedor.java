package com.breakingrules.stock.proveedores.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(regexp = "\\d{11}", message = "El CUIT debe tener 11 dígitos numéricos")
    private String cuit;

    @Email(message = "Email inválido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Pattern(regexp = "\\d{6,15}", message = "Teléfono inválido")
    private String telefono;

    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String direccion;

    private Boolean activo = true;
}