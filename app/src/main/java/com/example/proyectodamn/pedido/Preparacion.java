package com.example.proyectodamn.pedido;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Preparacion {
    private String id;
    private String nombre;
    private String descripcion;

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return nombre;
    }
}

