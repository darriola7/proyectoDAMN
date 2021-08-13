package com.example.proyectodamn.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectodamn.R;
import com.example.proyectodamn.local.AuthLocal;
import com.example.proyectodamn.api.APIInterface;
import com.example.proyectodamn.api.APIClient;
import com.example.proyectodamn.pedido.LocalPedido;
import com.example.proyectodamn.pedido.Preparacion;
import com.example.proyectodamn.pedido.RequestPedidoDTO;
import com.example.proyectodamn.pedido.PedidoResponse;
import com.example.proyectodamn.room.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    String mEmail;
    String mToken;

    APIInterface apiInterface;
    Context ctx;

    Spinner spinnerLocales;
    Spinner spinnerPreparaciones;
    EditText txtCantidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        ctx = this.getApplicationContext();

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#11CFC5"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        spinnerLocales = findViewById(R.id.slcLocal);
        spinnerPreparaciones = findViewById(R.id.slcPreparacion);
        txtCantidad = findViewById(R.id.txtCantidad);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser().observe(this, user -> {
            mEmail = user.getMEmail();
            mToken = user.getMToken();

            //Harcodeo localizacion porque no puedo obtenerla de forma automatica
            int latitud = 100;
            int longitud = 100;
            String tokenAuth = "Bearer " + mToken;
            try {
                Call<List<AuthLocal>> callAuthResponse = apiInterface.locales(tokenAuth, latitud, longitud);
                callAuthResponse.enqueue(new Callback<List<AuthLocal>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(@NotNull Call<List<AuthLocal>> call, @NotNull Response<List<AuthLocal>> response) {

                        if (response.code() == 200) {
                            List<LocalPedido> locales = new ArrayList<>();
                            assert response.body() != null;
                            response.body().forEach(localResult -> {
                                LocalPedido local = new LocalPedido(localResult._id, localResult.nombre);
                                locales.add(local);
                            });
                            loadLocales(locales);
                            loadPreparaciones();

                        } else {
                            Toast.makeText(ctx, "Oops, algo salió mal", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AuthLocal>> call, Throwable t) {
                        Log.d("MAIN_ACTIVITY_PEDIDO", "Error en el callback local" + t.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.d("LOCALES ERROR", "Exploto todo");
            }
        });
    }


    private void loadLocales(List<LocalPedido> list) {
        ArrayAdapter<LocalPedido> adapter = new ArrayAdapter<LocalPedido>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLocales.setAdapter(adapter);
    }

    private void loadPreparaciones() {
        //HARCODEO PREPARACIONES PORQUE NO TENGO GET DE PREPARACIONES EN API
        List<Preparacion> preparaciones = new ArrayList<>();
        Preparacion preparacion = new Preparacion("60ca55e653521306a41bed86", "Donna burger", "14- DONNA  * 150 g de carne * Bacon * Queso cheddar * Todo en medio de dos donas glaseadas");
        Preparacion preparacion2 = new Preparacion("60ca55e653521306a41bed86", "Classic burger", "14- DONNA  * 150 g de carne * Bacon * Queso cheddar * Todo en medio de dos donas glaseadas");
        preparaciones.add(preparacion);
        preparaciones.add(preparacion2);

        ArrayAdapter<Preparacion> adapter = new ArrayAdapter<Preparacion>(this, android.R.layout.simple_spinner_item, preparaciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPreparaciones.setAdapter(adapter);
    }

    public void goPedido(View view) {
        LocalPedido localPedido = (LocalPedido) spinnerLocales.getSelectedItem();
        Preparacion preparacionPedido = (Preparacion) spinnerPreparaciones.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText().toString());

        List<RequestPedidoDTO.Preparacion> preps = new ArrayList<>();
        RequestPedidoDTO.Preparacion preparacion = new RequestPedidoDTO.Preparacion(preparacionPedido.getId(), cantidad);
        preps.add(preparacion);
        RequestPedidoDTO dto = new RequestPedidoDTO(mEmail, localPedido.getId(), preps);
        String tokenAuth = "Bearer " + mToken;


        try {
            Call<PedidoResponse> callAuthResponse = apiInterface.pedido(tokenAuth, dto);
            callAuthResponse.enqueue(new Callback<PedidoResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<PedidoResponse> call, Response<PedidoResponse> response) {

                    if (response.code() == 200) {
                        assert response.body() != null;
                        Toast.makeText(ctx, "Llegará en: " + response.body().local.tiempoEntrega + "\n" + "Precio total con envío: "+ response.body().factura.total, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ctx, "Oops, algo salió mal", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PedidoResponse> call, Throwable t) {
                    Log.d("MAIN_ACTIVITY_PEDIDO", "Error en el callback pedido" + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("PEDIDO ERROR", "Exploto todo");
        }

    }
}