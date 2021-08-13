package com.example.proyectodamn.pedido;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PedidoResponse implements Serializable {

    @SerializedName("factura")
    public Factura factura;
    @SerializedName("local")
    public LocalPedido local;

    @AllArgsConstructor
    public class Factura {
        @SerializedName("total")
        public int total;

    }
}

