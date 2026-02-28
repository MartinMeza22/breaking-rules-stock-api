package com.breakingrules.stock.proveedores.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedores")
public class Proveedor {
    //Crear Proveedor (id, nombre, cuit, email, telefono, direccion, activo)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank(message = "El nombre es obligatorio")
        @Column(nullable = false)
        private String nombre;

        @NotBlank(message = "El CUIT es obligatorio")
        @Column(nullable = false, unique = true)
        private String cuit;

        @NotNull(message = "El email es obligatorio")
        private String email;

        @NotNull(message = "El telefono es obligatorio")
        private String telefono;

        @NotNull(message = "La dirección es obligatoria")
        private String direccion;

        @Column(nullable = false)
        private Boolean activo = true;
}
