package com.example.mobiledrive.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledrive.R;
import com.example.mobiledrive.model.Token;
import com.example.mobiledrive.service.ServerRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout phone;
    private TextInputLayout password;

    private String phoneValue;
    private String passwordValue;

    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, Window.FEATURE_ACTION_MODE_OVERLAY);
        setContentView(R.layout.activity_login);

        phone = (TextInputLayout) findViewById(R.id.phoneLogin);
        password = (TextInputLayout) findViewById(R.id.passwordLogin);

    }

    public void toRegisterPage(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            phoneValue = intent.getStringExtra("phone");
            passwordValue = intent.getStringExtra("password");

            phone.getEditText().setText(phoneValue);
            password.getEditText().setText(passwordValue);
        }
    }

    @SneakyThrows
    public void logIn(View view) {
        phoneValue = phone.getEditText().getText().toString();
        passwordValue = password.getEditText().getText().toString();

        sendLoginRequest();

        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        intent.putExtra("Token", token);

        LoginActivity.this.finish();
        LoginActivity.this.startActivity(intent);
    }

    private void sendLoginRequest() throws java.util.concurrent.ExecutionException, InterruptedException, JSONException {
        String phoneKey = "phone=" + phoneValue;
        String passwordKey = "password=" + passwordValue;
        String url = "/login?"+phoneKey+"&"+passwordKey;

        JSONObject getData = new ServerRequest().execute(url, "GET").get();

        String access_token = "Drive " + getData.get("access_token").toString();
        String refresh_token = "Drive " + getData.get("refresh_token").toString();
        token = new Token(access_token, refresh_token);
    }
}