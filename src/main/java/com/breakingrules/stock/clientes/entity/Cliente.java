package com.breakingrules.stock.clientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String apellido;

    @NotBlank(message = "El DNI o CUIT es obligatorio")
    @Size(max = 20, message = "Máximo 20 caracteres")
    private String documento;

    @Size(max = 11)
    @Pattern(regexp = "^$|^\\d{11}$", message = "CUIL inválido")
    private String cuil;

    @Email(message = "Email inválido")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String email;

    @Pattern(regexp = "\\d{6,15}", message = "Teléfono inválido")
    private String telefono;

    @Size(max = 200, message = "Máximo 200 caracteres")
    private String direccion;

    private Boolean activo = true;
}