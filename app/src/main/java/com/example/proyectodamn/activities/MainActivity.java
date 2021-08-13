package com.example.proyectodamn.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodamn.R;
import com.example.proyectodamn.login.UserResponse;
import com.example.proyectodamn.room.UserViewModel;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#11CFC5"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public void goLocales(View view) {
        Intent intent = new Intent(getBaseContext(), LocalActivity.class);
        startActivity(intent);
    }

    public void goPedido(View view) {
        Intent intent = new Intent(getBaseContext(), PedidoActivity.class);
        startActivity(intent);
    }

}