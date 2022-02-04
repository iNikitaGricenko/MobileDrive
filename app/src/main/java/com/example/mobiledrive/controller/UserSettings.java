package com.example.mobiledrive.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mobiledrive.R;
import com.example.mobiledrive.model.Token;
import com.example.mobiledrive.model.User;
import com.example.mobiledrive.service.ServerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UserSettings extends AppCompatActivity {

    private Token token;
    private User user;
    private TextView firstName, phone;
    String access_token;
    String refresh_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Intent intent = getIntent();
        token = (Token) intent.getSerializableExtra("Token");

        access_token = token.getAccessToken();
        refresh_token = token.getRefreshToken();

        firstName = findViewById(R.id.firstNameMain);
        phone = findViewById(R.id.phoneMain);

        getUserFromServer();

    }

    @SneakyThrows
    private void getUserFromServer() {
        Thread thread = new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                ResponseBody body = sendRequest();
                if (body == null) {
                    JSONObject getData = new ServerRequest().execute("http://10.0.2.2:8080/api/token/refresh", "GET").get();
                    access_token = getData.getString("access_token");
                    refresh_token = getData.getString("refresh_token");
                    token = new Token(access_token, refresh_token);
                    body = sendRequest();
                }
                ObjectMapper objectMapper = new ObjectMapper();
                user = objectMapper.readValue(body.string(), User.class);
            }
        };
        thread.start();
        thread.join();
        firstName.setText("Hello " + user.getFirstName());
        phone.setText(user.getPhone());
    }

    private ResponseBody sendRequest() throws IOException {
        return sendRequest("http://10.0.2.2:8080/api/user");
    }

    private ResponseBody sendRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token.getAccessToken())
                .build();

        Response response = client.newCall(request).execute();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e("mMessage", mMessage);
            }
        });

        if (response.code() == 403) {
            return null;
        }
        return response.body();
    }
}