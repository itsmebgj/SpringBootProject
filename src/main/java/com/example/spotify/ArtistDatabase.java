package com.example.spotify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtistDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/sign"; // 데이터베이스 URL
    private static final String USER = "bgj3088"; // 데이터베이스 사용자명
    private static final String PASSWORD = "123123"; // 데이터베이스 비밀번호

    // MySQL 연결
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 아티스트 목록 가져오기
    public static List<String> getArtistsFromDatabase() {
        List<String> artists = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            String query = "SELECT name FROM artists"; // artists 테이블에서 name 컬럼만 가져옴
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                artists.add(rs.getString("name")); // name 컬럼의 값을 리스트에 추가
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artists;
    }
}