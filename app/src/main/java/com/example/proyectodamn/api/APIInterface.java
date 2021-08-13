package com.example.proyectodamn.api;

import com.example.proyectodamn.local.AuthLocal;
import com.example.proyectodamn.login.UserResponse;
import com.example.proyectodamn.login.RequestUserDTO;
import com.example.proyectodamn.register.RequestRegisterDTO;
import com.example.proyectodamn.pedido.PedidoResponse;
import com.example.proyectodamn.pedido.RequestPedidoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("/login")
    Call<UserResponse> login(@Body RequestUserDTO user);

    @POST("/register")
    Call<UserResponse> register(@Body RequestRegisterDTO user);

    @GET("/locales")
    Call<List<AuthLocal>> locales(@Header("authorization") String tokenAuth, @Query("latitud") int latidud,
                                  @Query("longitud") int longitud);

    @POST("/pedido")
    Call<PedidoResponse> pedido(@Header("authorization") String tokenAuth, @Body RequestPedidoDTO pedido);
}
