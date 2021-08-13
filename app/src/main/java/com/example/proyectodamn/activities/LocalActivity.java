package com.example.proyectodamn.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.proyectodamn.R;
import com.example.proyectodamn.adapter.MainAdapter;
import com.example.proyectodamn.api.APIClient;
import com.example.proyectodamn.api.APIInterface;

import com.example.proyectodamn.local.AuthLocal;
import com.example.proyectodamn.login.UserResponse;
import com.example.proyectodamn.room.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    APIInterface apiInterface;
    Context ctx;

    EditText txtLatitud;
    EditText txtLongitud;
    ExpandableListView listLocales;

    String mEmail;
    String mToken;

    ArrayList<String> listGroup = new ArrayList<>();
    HashMap<String, ArrayList<String>> listChild = new HashMap<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        listLocales = findViewById(R.id.lstLocales);
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        ctx = this.getApplicationContext();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#11CFC5"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser().observe(this, user -> {
            mEmail = user.getMEmail();
            mToken = user.getMToken();
        });
    }


    public void getLocales(View view) {
        int latitud = Integer.parseInt(txtLatitud.getText().toString());
        int longitud = Integer.parseInt(txtLongitud.getText().toString());
        String tokenAuth = "Bearer " + mToken;
        try {
            Call<List<AuthLocal>> callAuthResponse = apiInterface.locales(tokenAuth, latitud,longitud);
            callAuthResponse.enqueue(new Callback<List<AuthLocal>>() {
                @Override
                public void onResponse(Call<List<AuthLocal>> call, Response<List<AuthLocal>> response) {

                    if (response.code() == 200) {
                        List<AuthLocal> locales = response.body();
                        assert locales != null;
                        loadLocales(locales);
                    } else {
                        Toast.makeText(ctx, "Oops, algo salió mal", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<List<AuthLocal>> call, @NotNull Throwable t) {
                    Log.d("MAIN_ACTIVITY_LOCAL", "Error en el callback local" + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("LOCALES ERROR", "Exploto todo");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void loadLocales(List<AuthLocal> locales) {
        locales.forEach((local) -> {
            listGroup.add(local.nombre);
            String nombre = local.nombre;
            String direccion = local.direccion;
            String geolocalizacion = "Lat: " + local.geolocalizacion.latitud +
                    " - Long:" + local.geolocalizacion.longitud;
            String tiempoEntrega = String.valueOf(local.tiempoEntrega);
            String costoEnvio = String.valueOf(local.costoEnvio);
            String horario = local.horario.abre + " - " + local.horario.cierra;
            String distancia = String.valueOf(local.distancia) + " km";
            String puntuacionPromedio = String.valueOf(local.puntuacionPromedio);

            ArrayList<String> datos = new ArrayList<String>();
            StringBuilder builderTags = new StringBuilder(local.tags.size());
            StringBuilder builderComentarios = new StringBuilder(local.comentarios.size());

            local.tags.forEach(tag -> {
                builderTags.append(tag + ", ");
            });

            local.comentarios.stream().forEach(c -> {
                String comentario = c.usuario.nombre + ": " + c.resenia;
                builderComentarios.append(comentario + ". // ");

            });

            datos.add("Nombre: " + nombre);
            datos.add("Dirección: " + direccion);
            datos.add("Geolocalización: " + geolocalizacion);
            datos.add("Puntuacion Promedio: " + puntuacionPromedio);
            datos.add("Comentarios: " + builderComentarios.toString());
            datos.add("Tiempo entrega: " + tiempoEntrega);
            datos.add("Costo envío: " + costoEnvio);
            datos.add("Tags: " + builderTags.toString());
            datos.add("Horario: " + horario);
            datos.add("Distancia: " + distancia);

            listChild.put(local.nombre, datos);
        });
        adapter = new MainAdapter(listGroup, listChild);
        listLocales.setAdapter(adapter);
    }
}