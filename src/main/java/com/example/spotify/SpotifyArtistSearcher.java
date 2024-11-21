package com.example.spotify;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Scanner;

public class SpotifyArtistSearcher {

    public static void main(String[] args) {
        try {
            // 1. 인증 URL 생성
            String authorizationUrl = SpotifyTokenManager.getAuthorizationUrl();
            System.out.println("Go to this URL to authorize the app: ");
            System.out.println(authorizationUrl);

            // 2. 인증 코드 입력 받기
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the authorization code from the URL:");
            String authorizationCode = scanner.nextLine();
            scanner.close();

            // 3. Authorization Code로 Access Token 발급 받기
            String accessToken = SpotifyTokenManager.getAccessToken(authorizationCode);

            // 4. 동적으로 토큰을 받은 후 아티스트 검색
            searchArtist(accessToken, "Ed Sheeran");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Spotify에서 아티스트 검색
    private static void searchArtist(String accessToken, String artistName) throws Exception {
        String encodedArtistName = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
        String url = "https://api.spotify.com/v1/search?q=" + encodedArtistName + "&type=artist&limit=1"; // 최대 1명만 검색

        // HTTP GET 요청 생성
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + accessToken); // 발급받은 액세스 토큰을 헤더에 추가

        // API 요청을 통해 아티스트 정보를 가져옴
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                // 정상 응답이 오면 아티스트 ID를 파싱
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonArray artists = jsonObject.getAsJsonObject("artists").getAsJsonArray("items");

                if (artists.size() > 0) {
                    JsonObject artist = artists.get(0).getAsJsonObject();
                    String artistId = artist.get("id").getAsString(); // 아티스트 고유 ID
                    System.out.println("Artist Name: " + artistName);
                    System.out.println("Artist ID: " + artistId); // 아티스트 고유 ID 출력
                } else {
                    System.out.println("아티스트를 찾을 수 없습니다.");
                }
            } else {
                System.out.println("검색에 실패했습니다. 상태 코드: " + statusCode);
            }
        }
    }
}
