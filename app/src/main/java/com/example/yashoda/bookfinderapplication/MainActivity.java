package com.example.yashoda.bookfinderapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    Connectivity connectivity = new Connectivity();

    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_weight);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Button btnLogin = findViewById(R.id.LoginOnMain);
        Button btnRegister = findViewById(R.id.RegisterOnMain);
        createLoginBtn(btnLogin);
        createRegisterBtn(btnRegister);
    }

    private void createLoginBtn(Button btnLogin) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LogInActivity.class));
                finish();
            }
        });
    }

    private void createRegisterBtn(Button btnRegister) {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
                finish();
            }
        });
    }

}
