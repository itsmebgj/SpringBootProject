package com.example.spotify;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Scanner;
import java.util.Base64;

public class SpotifyRecommendation {

    private static final String CLIENT_ID = "b03973dd37b54b81a9095638ae54fe98"; // Spotify 대시보드 클라이언트 ID
    private static final String CLIENT_SECRET = "72497d1f6d2940a88409342d0db102fe"; // Spotify 대시보드 클라이언트 시크릿
    private static final String REDIRECT_URI = "http://localhost:8080/callback"; // 리디렉션 URI

    private static String accessToken = ""; // 액세스 토큰
    private static String refreshToken = ""; // 리프레시 토큰

    public static void main(String[] args) {
        try {
            // 1. 사용자에게 인증 URL을 보여주어 로그인 및 권한 부여를 받음
            String authorizationUrl = getAuthorizationUrl();
            System.out.println("Go to this URL to authorize the app: ");
            System.out.println(authorizationUrl);

            // 2. 사용자로부터 인증 코드를 입력받음
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the authorization code from the URL:");
            String authorizationCode = scanner.nextLine();
            scanner.close();

            // 3. Authorization Code를 사용하여 Access Token과 Refresh Token 발급
            requestAccessToken(authorizationCode);

            // 4. 추천 곡 가져오기
            getRecommendedSongs();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 인증 URL 생성
    private static String getAuthorizationUrl() {
        return "https://accounts.spotify.com/authorize?" +
                "client_id=" + CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=user-library-read user-top-read playlist-read-private user-read-private playlist-modify-private playlist-modify-public";
    }

    // Authorization Code를 통해 Access Token과 Refresh Token 발급
    private static void requestAccessToken(String authorizationCode) throws Exception {
        String tokenUrl = "https://accounts.spotify.com/api/token";
        HttpPost post = new HttpPost(tokenUrl);
        post.setHeader("Authorization", "Basic " + getBase64Credentials());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=authorization_code" +
                "&code=" + authorizationCode +
                "&redirect_uri=" + REDIRECT_URI;
        post.setEntity(new StringEntity(body));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(post);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Access Token과 Refresh Token 추출
        accessToken = jsonObject.get("access_token").getAsString();
        refreshToken = jsonObject.get("refresh_token").getAsString();

        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);
        client.close();
    }

    // Base64로 인코딩된 클라이언트 자격 증명 생성
    private static String getBase64Credentials() {
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    // 추천 곡을 요청하는 함수
    private static void getRecommendedSongs() throws Exception {
        // 추천을 위한 기본 장르와 아티스트 예시
        String genres = ""; // 추천에 사용할 장르
        String artists = "7c1HgFDe8ogy5NOZ1ANCJQ,1rp5HzWaNwgauM5W5YmZ3U,1rpgxJZxZMLnFNc1Jmyov5,5TnQc2N1iKlFjYD7CPGvFc"; // 추천에 사용할 아티스트 (카더가든,이창섭,YB,day6)

        // 추천 곡을 위한 API URL
        String url = "https://api.spotify.com/v1/recommendations?seed_genres=" + genres + "&seed_artists=" + artists + "&limit=100";

        // HTTP GET 요청 생성
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + accessToken); // 발급받은 액세스 토큰을 헤더에 추가

        // API 요청을 통해 추천 곡을 가져옴
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                // 정상 응답이 오면 추천 곡 리스트 파싱
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonArray tracks = jsonObject.getAsJsonArray("tracks");

                System.out.println("추천된 곡들:");
                for (int i = 0; i < tracks.size(); i++) {
                    JsonObject track = tracks.get(i).getAsJsonObject();
                    String songName = track.get("name").getAsString();
                    String artistName = track.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                    System.out.println(songName + " - " + artistName);
                }
            } else {
                System.out.println("추천 곡을 가져오는 데 실패했습니다. 상태 코드: " + statusCode);
            }
        }
    }
}
