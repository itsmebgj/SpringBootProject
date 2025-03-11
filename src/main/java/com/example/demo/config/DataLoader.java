package com.example.demo.config;

import com.example.demo.service.ArtistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ArtistService artistService;

    public DataLoader(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public void run(String... args) throws Exception {
        artistService.saveArtists();  // 애플리케이션 실행 시 아티스트 저장
    }
}
