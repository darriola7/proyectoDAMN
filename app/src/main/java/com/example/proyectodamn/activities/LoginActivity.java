package com.example.proyectodamn.activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectodamn.R;
import com.example.proyectodamn.api.APIInterface;
import com.example.proyectodamn.api.APIClient;
import com.example.proyectodamn.login.UserResponse;
import com.example.proyectodamn.login.RequestUserDTO;
import com.example.proyectodamn.room.UserTable;
import com.example.proyectodamn.room.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    public static final String ACTIVITY_LOG_TAG = "MAIN-ACTIVITY";

    private EditText txtUsername;
    private EditText txtPassword;
    private UserViewModel mUserViewModel;
    APIInterface apiInterface;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = (EditText)findViewById(R.id.editTextEmail);
        txtPassword = (EditText)findViewById(R.id.editTextPassword);
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        ctx = this.getApplicationContext();
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    public void onRegisterClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    public void onLogin(View view) {
            String user = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            RequestUserDTO dto = new RequestUserDTO(user, password);
            try {
                Call<UserResponse> callUserResponse = apiInterface.login(dto);
                callUserResponse.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<UserResponse> call, @NotNull Response<UserResponse> response) {
                        if(response.code()==200){
                            assert response.body() != null;
                            String email = response.body().user.getEmail();
                            String token = response.headers().get("Authorization");
                            Log.i("email", email);
                            UserTable user = new UserTable(email,token);
                            mUserViewModel.insert(user);
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(ctx,"Revise email y/o contraseña",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.d("MAIN_ACTIVITY_LOGIN", "Error en el login" + t.getMessage());

                    }
                });
            } catch (Exception e) {
                Log.d("ERROR LOGIN", "Exploto todo");
            }
    }
}