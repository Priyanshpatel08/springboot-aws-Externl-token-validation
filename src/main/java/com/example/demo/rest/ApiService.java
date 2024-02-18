package com.example.demo.rest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApiService {

    @Value("${token.validate.api.url}")
    private String apiUrl;

    private final OkHttpClient okHttpClient;

    public ApiService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public boolean callApi(String authorizationToken) throws IOException {
        // Build the request
        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", authorizationToken)
                .build();

        // Execute the request
        try (Response response = okHttpClient.newCall(request).execute()) {
            // Check if the request was successful (status code 200 OK)
            if (response.isSuccessful()) {
                return true;
            } else {
                // Handle the error response
            	System.out.println("Invalid Authorization Token...!!!");
                return false;
            }
        }
    }
}
