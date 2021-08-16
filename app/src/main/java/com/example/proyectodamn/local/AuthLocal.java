package com.example.proyectodamn.local;

import com.example.proyectodamn.login.AuthUser;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthLocal implements Serializable {

    @SerializedName("_id")
    public String _id;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("direccion")
    public String direccion;
    @SerializedName("geolocalizacion")
    public Geolocalizacion geolocalizacion;
    @SerializedName("puntuaciones")
    public List<Puntuacion> puntuaciones;
    @SerializedName("puntuacionPromedio")
    public int puntuacionPromedio;
    @SerializedName("comentarios")
    public List<Comentario> comentarios;
    @SerializedName("tiempoEntrega")
    public int tiempoEntrega;
    @SerializedName("costoEnvio")
    public int costoEnvio;
    @SerializedName("tags")
    public List<String> tags;
    @SerializedName("horario")
    public Horario horario;
    @SerializedName("distancia")
    public int distancia;

    @AllArgsConstructor
    public static class Geolocalizacion implements Serializable {
        @SerializedName("longitud")
        public double longitud;
        @SerializedName("latitud")
        public double latitud;

    }

    @AllArgsConstructor
    public static class Comentario implements Serializable {

        @SerializedName("reseña")
        public String resenia;
        @SerializedName("usuario")
        public AuthUser usuario;

    }

    @AllArgsConstructor
    public static class Puntuacion implements Serializable {

        @SerializedName("_id")
        public String id;
        @SerializedName("numero")
        public int numero;
        @SerializedName("usuario")
        public String usuario;

    }

    @AllArgsConstructor
    public static class Horario implements Serializable {

        @SerializedName("abre")
        public int abre;
        @SerializedName("cierra")
        public int cierra;

    }
}
