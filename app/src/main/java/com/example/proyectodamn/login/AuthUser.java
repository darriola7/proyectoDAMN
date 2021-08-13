package com.example.proyectodamn.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthUser implements Serializable {

    @SerializedName("_id")
    public String id;
    @SerializedName("nombre")
    public String nombre;
    @SerializedName("apellido")
    public String apellido;
    @SerializedName("email")
    public String email;
    @SerializedName("telefono")
    public int telefono;
    @SerializedName("contrasenia")
    public String contrasenia;
    @SerializedName("ci")
    public String ci;
}
