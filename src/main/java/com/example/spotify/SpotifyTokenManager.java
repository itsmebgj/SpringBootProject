package com.example.spotify;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Base64;
import java.util.Scanner;

public class SpotifyTokenManager {
    private static final String CLIENT_ID = "b03973dd37b54b81a9095638ae54fe98"; // Spotify 클라이언트 ID
    private static final String CLIENT_SECRET = "72497d1f6d2940a88409342d0db102fe"; // Spotify 클라이언트 시크릿
    private static final String REDIRECT_URI = "http://localhost:8080/callback"; // 리디렉션 URI
    private static String accessToken = ""; // Access Token
    private static String refreshToken = ""; // Refresh Token

    public static void main(String[] args) {
        try {
            // 사용자에게 인증 URL 제공 및 인증 코드 입력받기
            String authorizationUrl = getAuthorizationUrl();
            System.out.println("Go to this URL to authorize the app: ");
            System.out.println(authorizationUrl);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the authorization code from the URL:");
            String authorizationCode = scanner.nextLine();
            scanner.close();

            // Authorization Code를 통해 Access Token과 Refresh Token 발급
            requestAccessToken(authorizationCode);

            // Access Token 만료 상황을 시뮬레이션하고 Refresh Token으로 Access Token 재발급
            System.out.println("Simulating token expiration...");
            refreshAccessToken();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAuthorizationUrl() {
        return "https://accounts.spotify.com/authorize?" +
                "client_id=" + CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=user-library-read"; // 필요한 권한 설정
    }

    private static void requestAccessToken(String authorizationCode) throws Exception {
        String tokenUrl = "https://accounts.spotify.com/api/token";
        HttpPost post = new HttpPost(tokenUrl);
        post.setHeader("Authorization", "Basic " + getBase64Credentials());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=authorization_code" +
                      "&code=" + authorizationCode +
                      "&redirect_uri=" + REDIRECT_URI;
        post.setEntity(new StringEntity(body));

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // Access Token과 Refresh Token 저장
            accessToken = jsonObject.get("access_token").getAsString();
            refreshToken = jsonObject.get("refresh_token").getAsString();

            System.out.println("Access Token: " + accessToken);
            System.out.println("Refresh Token: " + refreshToken);
        }
    }

    private static void refreshAccessToken() throws Exception {
        String tokenUrl = "https://accounts.spotify.com/api/token";
        HttpPost post = new HttpPost(tokenUrl);
        post.setHeader("Authorization", "Basic " + getBase64Credentials());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=refresh_token" +
                      "&refresh_token=" + refreshToken;
        post.setEntity(new StringEntity(body));

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // 새로운 Access Token 저장
            accessToken = jsonObject.get("access_token").getAsString();
            System.out.println("새로운 Access Token: " + accessToken);
        }
    }

    private static String getBase64Credentials() {
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
