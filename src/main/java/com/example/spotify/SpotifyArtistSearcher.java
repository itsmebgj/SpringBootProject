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
import java.util.List;

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

            // 4. MySQL에서 아티스트 목록 가져오기
            List<String> artistsToSearch = ArtistDatabase.getArtistsFromDatabase(); // 데이터베이스에서 아티스트 이름 가져오기
            
            /* 4. 동적으로 토큰을 받은 후 아티스트 검색
            String[] artistsToSearch = 
            {"Jimin", "aespa", "Jung Kook", "DAY6", "Bruno Mars", "NewJeans", "G-DRAGON", "BTS", "LE SSERAFIM", "BIGBANG", "ROSÉ", "PLAVE", "V", "Taylor Swift", 
            "ILLIT", "TOMORROW X TOGETHER", "LEE SEUNG YOON", "BABYMONSTER", "Kanye West", "Justin Bieber", "SEVENTEEN", "IU", "The Weeknd", "KISS OF LIFE", "Jin", 
            "Ariana Grande", "AKMU", "Tyler, The Creator", "NCT DREAM", "Latto", "Billie Eilish", "Charlie Puth", "Sabrina Carpenter", "BIG Naughty", "Loco", "BOL4", 
            "Zion.T", "Kenshi Yonezu", "JENNIE", "BOYNEXTDOOR", "RIIZE", "fromis_9", "The Black Skirts", "QWER", "Post Malone", "JANNABI", "Lady Gaga", "ENHYPEN", 
            "Stray Kids", "Travis Scott", "SZA", "TAEYEON", "BLACKPINK", "Yorushika", "Ed Sheeran", "Leellamarz", "wave to earth", "Car, the garden", "Lauv", 
            "Yerin Baek", "Jack Harlow", "IVE", "10CM", "Frank Ocean", "YOASOBI", "Nerd Connection", "Vaundy", "Coldplay", "Maroon 5", "Kendrick Lamar", "LUCY", 
            "Drake", "Lee Mujin", "Red Velvet", "keshi", "HEIZE", "d4vd", "데이먼스 이어 Damons year", "Daniel Caesar", "MINHO", "OFFICIAL HIGE DANDISM", "WOODZ", 
            "HYUKOH", "YANGHONGWON", "League of Legends", "DEAN", "Younha", "Playboi Carti", "CHANGMO", "NMIXX", "RADWIMPS", "Imagine Dragons", "D.O.", "ZICO", 
            "Crush", "Jay Park", "Yuuri", "Roy Kim", "GIRIBOY", "TWICE", "Linkin Park", "TOIL", "XXXTENTACION", "Sung Si Kyung", "EXO", "Oasis", "ZUTOMAYO", 
            "One Direction", "Fujii Kaze", "Mrs. GREEN APPLE", "Eminem", "BAEKHYUN", "Choi Yu Ree", "(G)I-DLE", "Agust D", "george", "NCT 127", "NCT WISH", 
            "ASH ISLAND", "Dua Lipa", "Sofia Carson", "Lee Moon Sae", "M.C the Max", "2NE1", "Kim Seungmin", "Future", "TWS", "LISA", "XG", "Shawn Mendes", 
            "PATEKO", "King Gnu", "Lim Jae Hyun", "Metro Boomin", "Jeong Dong Won", "HOMIES", "YENA", "OneRepublic", "Shyboiitobii", "Doja Cat", "Beenzino", 
            "KATSEYE", "Paul Kim", "Ty Dolla $ign", "Kid Milli", "Benson Boone", "Charli xcx", "David Guetta", "Ado", "Radiohead", "FIFTY FIFTY", "GroovyRoom", 
            "Jukjae", "ITZY", "Lil Uzi Vert", "Silica Gel", "Lee Young Ji", "Harry Styles", "Sia", "Alan Walker", "JAEHYUN", "Gracie Abrams", "The Kid LAROI", 
            "Cigarettes After Sex", "BE'O", "LeeHi", "Woody", "The Beatles", "Epik High", "Troye Sivan", "HAON", "Aimyon", "The Chainsmokers", "TAEMIN", "BUZZ", 
            "Olivia Rodrigo", "21 Savage", "Heo Hoy Kyung", "Sam Smith", "LANY", "CODE KUNST", "Park Hyo Shin", "JVKE", "PSY", "Lim Changjung", "Lana Del Rey", 
            "MAKTUB", "Kid Wine", "MeloMance", "Primary", "BTOB", "N.Flying", "Chappell Roan", "j-hope", "HANRORO", "DAVICHI", "Creepy Nuts", "Punch", "DPR LIVE", "SPYAIR"}; // 검색할 아티스트 목록
            //https://charts.spotify.com/charts/view/artist-kr-weekly/latest
            */

            for (String artistName : artistsToSearch) {
                searchArtist(accessToken, artistName);
            }

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
