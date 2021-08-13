package com.example.proyectodamn.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserResponse implements Serializable {

    @SerializedName("user")
    public AuthUser user;

    public UserResponse() {}
}
