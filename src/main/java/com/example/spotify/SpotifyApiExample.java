package com.example.spotify;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SpotifyApiExample {
    private static final String CLIENT_ID = "b03973dd37b54b81a9095638ae54fe98"; // 클라이언트 ID
    private static final String CLIENT_SECRET = "72497d1f6d2940a88409342d0db102fe"; // 클라이언트 시크릿
    private static String accessToken = "BQCSIWevJ4-VpPMmujJ0UDYyo3GCujXOo0uCsqQo6tj5-hJthAh6k7QkeZ3WSRATmEfjWJ4d5rgOMNIdCJcoSWw97j2wylpcA1n39Z3ASn5NBxDEoyCqtTZsadbHUt4v0kq_pj8714AON4kIabxv5E2l7C_GRcT110A6YZTNQbkhZqg_xRdZjKF3GTH7eSB6N02zuUKQZpCdx4exCG_eXZ0jFaercQ"; // 초기 Access Token
    private static final String refreshToken = "AQCsu_4enrERgM8XO8OFPb_x_qGjwRPirZsBqPb6_n-Q2osTWIMxAa1QC_PvZTe5fk5tDeTQq9KB_yBqnx1hGAjFbteYBdNOYIH0xcuYjl1Y7Nlgty1Kv_-dkdIs9KoHF20"; // Refresh Token

    public static void main(String[] args) {
        try {
            // 사용자 정보 가져오기
            getUserInfo();

            // 새로운 플레이리스트 생성
            createPlaylist("My New Playlist", "This is my new playlist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getUserInfo() throws Exception {
        String url = "https://api.spotify.com/v1/me";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + accessToken);

        CloseableHttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 401) { // Access Token 만료
            accessToken = refreshAccessToken();
            request.setHeader("Authorization", "Bearer " + accessToken);
            response = client.execute(request);
        }
        
        String jsonResponse = EntityUtils.toString(response.getEntity());
        JsonObject userInfo = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // 사용자 정보 출력
        System.out.println("User Info: " + userInfo);
        client.close();
    }

    private static void createPlaylist(String playlistName, String description) throws Exception {
        String url = "https://api.spotify.com/v1/users/" + getUserId() + "/playlists";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/json");

        // 플레이리스트 정보 JSON 생성
        JsonObject playlistInfo = new JsonObject();
        playlistInfo.addProperty("name", playlistName);
        playlistInfo.addProperty("description", description);
        playlistInfo.addProperty("public", false);  // 비공개 설정

        StringEntity entity = new StringEntity(playlistInfo.toString());
        post.setEntity(entity);

        CloseableHttpResponse response = client.execute(post);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        JsonObject createdPlaylist = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // 생성된 플레이리스트 정보 출력
        System.out.println("Created Playlist: " + createdPlaylist);
        client.close();
    }

    private static String getUserId() throws Exception {
        // 사용자 정보를 가져와서 ID 반환
        String url = "https://api.spotify.com/v1/me";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + accessToken);

        CloseableHttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 401) { // Access Token 만료
            accessToken = refreshAccessToken();
            request.setHeader("Authorization", "Bearer " + accessToken);
            response = client.execute(request);
        }

        String jsonResponse = EntityUtils.toString(response.getEntity());
        JsonObject userInfo = JsonParser.parseString(jsonResponse).getAsJsonObject();

        String userId = userInfo.get("id").getAsString();
        client.close();
        return userId;
    }

    private static String refreshAccessToken() throws Exception {
        String url = "https://accounts.spotify.com/api/token";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", "Basic " + getBase64Credentials());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=refresh_token&refresh_token=" + refreshToken;
        post.setEntity(new StringEntity(body));

        CloseableHttpResponse response = client.execute(post);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // 새로운 Access Token 반환
        String newAccessToken = jsonObject.get("access_token").getAsString();
        client.close();
        return newAccessToken;
    }

    private static String getBase64Credentials() {
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
