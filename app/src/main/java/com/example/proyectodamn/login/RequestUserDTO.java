package com.example.proyectodamn.login;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RequestUserDTO {

    @SerializedName("email")
    public String email;
    @SerializedName("contrasenia")
    public String contrasenia;

}
