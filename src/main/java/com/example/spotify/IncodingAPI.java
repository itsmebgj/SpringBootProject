package com.example.spotify;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IncodingAPI {
    public static void main(String[] args) throws IOException {
        String clientId = "b03973dd37b54b81a9095638ae54fe98";
        String clientSecret = "72497d1f6d2940a88409342d0db102fe";

        // Encode client ID and Secret to Base64
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        // Set up API request URL and settings
        URL url = new URL("https://accounts.spotify.com/api/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // Configure request body
        String requestBody = "grant_type=client_credentials";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Check and print the response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {  // HTTP 200 check
            InputStream responseStream = connection.getInputStream();
            String response = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Access Token Response: " + response);
        } else {
            System.out.println("Error: " + responseCode);
        }
    }
}