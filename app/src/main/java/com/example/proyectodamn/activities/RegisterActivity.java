package com.example.proyectodamn.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectodamn.R;
import com.example.proyectodamn.api.APIClient;
import com.example.proyectodamn.api.APIInterface;
import com.example.proyectodamn.login.UserResponse;
import com.example.proyectodamn.register.RequestRegisterDTO;
import com.example.proyectodamn.room.UserTable;
import com.example.proyectodamn.room.UserViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtEmail;
    private EditText txtTelefono;
    private EditText txtPassword;
    private EditText txtCedula;
    private UserViewModel mUserViewModel;
    APIInterface apiInterface;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtNombre = findViewById(R.id.editTextName);
        txtApellido = findViewById(R.id.editTextLastname);
        txtEmail = findViewById(R.id.editTextEmail);
        txtTelefono = findViewById(R.id.editTextMobile);
        txtPassword = findViewById(R.id.editTextPassword);
        txtCedula = findViewById(R.id.editTextDocumentID);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        ctx = this.getApplicationContext();
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void onRegister(View view) {
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String email = txtEmail.getText().toString();
        Integer telefono = Integer.parseInt(txtTelefono.getText().toString());
        String cedula = txtCedula.getText().toString();
        String password = txtPassword.getText().toString();
        String status = "ACTIVO";

        RequestRegisterDTO dto = new RequestRegisterDTO(nombre, apellido, email, telefono, password, cedula, status);

        try {
            Call<UserResponse> callAuthResponse = apiInterface.register(dto);
            callAuthResponse.enqueue(new Callback<UserResponse>() {

                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        Toast.makeText(ctx, "Registrado correctamente", Toast.LENGTH_LONG).show();
                        UserResponse data = response.body();
                        String token = response.headers().get("Authorization");
                        assert data != null;
                        UserTable user = new UserTable(data.user.getEmail(),token);
                        mUserViewModel.insert(user);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ctx, "Revise los datos ingresados.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UserResponse> call, @NotNull Throwable t) {
                    Log.d("MAIN_ACTIVITY_LOGIN", "Register error" + t.getMessage());

                }
            });
        } catch (Exception e) {
            Log.d("ERROR LOGIN", "Exploto todo");
        }
    }
}