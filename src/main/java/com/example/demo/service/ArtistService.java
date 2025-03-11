package com.example.demo.service;

import com.example.demo.model.Artist;
import com.example.demo.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public void saveArtists() {
        String[] artistsToSearch = {
            "Jimin", "aespa", "Jung Kook", "DAY6", "Bruno Mars", "NewJeans", "G-DRAGON", "BTS", 
            "LE SSERAFIM", "BIGBANG", "ROSÉ", "PLAVE", "V", "Taylor Swift", "ILLIT", "TOMORROW X TOGETHER",
            "LEE SEUNG YOON", "BABYMONSTER", "Kanye West", "Justin Bieber", "SEVENTEEN", "IU",
            "The Weeknd", "KISS OF LIFE", "Jin", "Ariana Grande", "AKMU", "Tyler, The Creator",
            "NCT DREAM", "Latto", "Billie Eilish", "Charlie Puth", "Sabrina Carpenter", "BIG Naughty",
            "Loco", "BOL4", "Zion.T", "Kenshi Yonezu", "JENNIE", "BOYNEXTDOOR", "RIIZE", "fromis_9",
            "The Black Skirts", "QWER", "Post Malone", "JANNABI", "Lady Gaga", "ENHYPEN", "Stray Kids",
            "Travis Scott", "SZA", "TAEYEON", "BLACKPINK", "Yorushika", "Ed Sheeran", "Leellamarz",
            "wave to earth", "Car, the garden", "Lauv", "Yerin Baek", "Jack Harlow", "IVE", "10CM",
            "Frank Ocean", "YOASOBI", "Nerd Connection", "Vaundy", "Coldplay", "Maroon 5",
            "Kendrick Lamar", "LUCY", "Drake", "Lee Mujin", "Red Velvet", "keshi", "HEIZE",
            "d4vd", "데이먼스 이어 Damons year", "Daniel Caesar", "MINHO", "OFFICIAL HIGE DANDISM",
            "WOODZ", "HYUKOH", "YANGHONGWON", "League of Legends", "DEAN", "Younha",
            "Playboi Carti", "CHANGMO", "NMIXX", "RADWIMPS", "Imagine Dragons", "D.O.", "ZICO",
            "Crush", "Jay Park", "Yuuri", "Roy Kim", "GIRIBOY", "TWICE", "Linkin Park", "TOIL",
            "XXXTENTACION", "Sung Si Kyung", "EXO", "Oasis", "ZUTOMAYO", "One Direction",
            "Fujii Kaze", "Mrs. GREEN APPLE", "Eminem", "BAEKHYUN", "Choi Yu Ree", "(G)I-DLE",
            "Agust D", "george", "NCT 127", "NCT WISH", "ASH ISLAND", "Dua Lipa", "Sofia Carson",
            "Lee Moon Sae", "M.C the Max", "2NE1", "Kim Seungmin", "Future", "TWS", "LISA", "XG",
            "Shawn Mendes", "PATEKO", "King Gnu", "Lim Jae Hyun", "Metro Boomin", "Jeong Dong Won",
            "HOMIES", "YENA", "OneRepublic", "Shyboiitobii", "Doja Cat", "Beenzino", "KATSEYE",
            "Paul Kim", "Ty Dolla $ign", "Kid Milli", "Benson Boone", "Charli xcx", "David Guetta",
            "Ado", "Radiohead", "FIFTY FIFTY", "GroovyRoom", "Jukjae", "ITZY", "Lil Uzi Vert",
            "Silica Gel", "Lee Young Ji", "Harry Styles", "Sia", "Alan Walker", "JAEHYUN",
            "Gracie Abrams", "The Kid LAROI", "Cigarettes After Sex", "BE'O", "LeeHi", "Woody",
            "The Beatles", "Epik High", "Troye Sivan", "HAON", "Aimyon", "The Chainsmokers",
            "TAEMIN", "BUZZ", "Olivia Rodrigo", "21 Savage", "Heo Hoy Kyung", "Sam Smith", "LANY",
            "CODE KUNST", "Park Hyo Shin", "JVKE", "PSY", "Lim Changjung", "Lana Del Rey",
            "MAKTUB", "Kid Wine", "MeloMance", "Primary", "BTOB", "N.Flying", "Chappell Roan",
            "j-hope", "HANRORO", "DAVICHI", "Creepy Nuts", "Punch", "DPR LIVE", "SPYAIR"
        };

        List<Artist> artistList = Arrays.stream(artistsToSearch)
        .filter(name -> !artistRepository.existsByName(name)) // 중복 방지
        .map(Artist::new)
        .collect(Collectors.toList());

        artistRepository.saveAll(artistList);
    }
}
