package com.example.mobiledrive.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequest extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... params) {
        return post(params);
    }

    private JSONObject post(String[] params) {
        String uldId = params[0];
        String method = params[1];
        String bytes = "";
        if (params.length > 2) {
            bytes = params[2];
        }
        String service = "http://10.0.2.2:8080"+ uldId;

        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(service).openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            DataOutputStream streamWriter = new DataOutputStream(httpURLConnection.getOutputStream());
            streamWriter.writeBytes(bytes);
            streamWriter.flush();
            streamWriter.close();

            httpURLConnection.connect();

            Log.d("response message", httpURLConnection.getResponseMessage());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return new JSONObject(stringBuffer.toString());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        Log.e("TAG", response.toString());
    }
}
