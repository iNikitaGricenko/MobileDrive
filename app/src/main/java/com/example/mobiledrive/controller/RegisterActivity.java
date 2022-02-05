package com.example.mobiledrive.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledrive.R;
import com.example.mobiledrive.service.ServerRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout firstName;
    private TextInputLayout secondName;
    private TextInputLayout surname;
    private TextInputLayout cityAutoComplete;
    private TextInputLayout phone;
    private TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, Window.FEATURE_ACTION_MODE_OVERLAY);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {
        firstName = (TextInputLayout) findViewById(R.id.firstNameRegister);
        secondName = (TextInputLayout) findViewById(R.id.secondNameRegister);
        surname = (TextInputLayout) findViewById(R.id.surnameRegister);
        cityAutoComplete = (TextInputLayout) findViewById(R.id.cityAutoComplete);
        phone = (TextInputLayout) findViewById(R.id.phoneRegister);
        password = (TextInputLayout) findViewById(R.id.passwordRegister);
    }

    public void registerUser(View view) {

        Intent intent = new Intent();
        register();

        String phoneValue = phone.getEditText().getText().toString();
        String passwordValue = password.getEditText().getText().toString();

        intent.putExtra("phone", phoneValue);
        intent.putExtra("password", passwordValue);

        setResult(RESULT_OK, intent);
        finish();
    }

    @SneakyThrows
    private void register() {
        JSONObject postData = new JSONObject();
        postData.put("firstName", firstName.getEditText().getText().toString());
        postData.put("secondName", secondName.getEditText().getText().toString());
        postData.put("surname", surname.getEditText().getText().toString());
        postData.put("city", cityAutoComplete.getEditText().getText().toString());
        postData.put("phone", phone.getEditText().getText().toString());
        postData.put("password", password.getEditText().getText().toString());

        new ServerRequest().execute("/api/user", "POST", postData.toString()).get().toString();
    }

}