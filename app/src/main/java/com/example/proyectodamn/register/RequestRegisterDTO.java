package com.example.proyectodamn.register;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RequestRegisterDTO {

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
    @SerializedName("status")
    public String status;

}
