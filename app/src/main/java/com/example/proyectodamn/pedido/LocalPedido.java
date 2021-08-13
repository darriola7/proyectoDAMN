package com.example.proyectodamn.pedido;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalPedido {
    private String id;
    private String nombre;
    public String tiempoEntrega;


    public LocalPedido(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.tiempoEntrega = tiempoEntrega;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return nombre;
    }
}
