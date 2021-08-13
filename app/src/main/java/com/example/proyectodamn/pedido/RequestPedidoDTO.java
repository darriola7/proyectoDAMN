package com.example.proyectodamn.pedido;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class RequestPedidoDTO {

    @SerializedName("email")
    public String email;
    @SerializedName("local_id")
    public String local_id;
    @SerializedName("preparaciones")
    public List<Preparacion> preparaciones;

    @AllArgsConstructor
    public static class Preparacion {
        @SerializedName("_id")
        public String _id;
        @SerializedName("cantidad")
        public int cantidad;

    }
}


