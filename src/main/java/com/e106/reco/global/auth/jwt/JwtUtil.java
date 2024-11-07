package com.e106.reco.global.auth.jwt;

import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.entity.Gender;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey secertKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret){
        this.secertKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }
    //1
    public String getNickname(String token){
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }
    //2
    public Long getSeq(String token) {
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("seq", Long.class);
    }
    //2
    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public Position getPosition(String token) {
        return Position.valueOf(Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("position", String.class));
    }

    public Gender getGender(String token) {
        return Gender.valueOf(Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("gender", String.class));
    }

    public Region getRegion(String token) {
       return Region.valueOf(Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("region", String.class));
    }

    public Genre getGenre(String token) {
        return Genre.valueOf(Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("genre", String.class));
    }

    public String getYear(String token) {
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("year", String.class);
    }

    public String getCrews(String token) {
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().get("crews", String.class);
    }


    //5
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secertKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }



    // 1 2 3 토큰의 특정요소 검증

    //4
    public String createJwt(String category, String nickname, Long seq, String email
                            , Position position, Gender gender, Genre genre, String Year, Region region
                            , String crews, Long expireMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("nickname", nickname)
                .claim("seq", seq)
                .claim("email", email)
                .claim("position", position)
                .claim("gender", gender)
                .claim("genre", genre)
                .claim("year", Year)
                .claim("region", region)
                .claim("crews", crews)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expireMs))
                .signWith(secertKey)
                .compact();
    }
    // 이름, 아이디, 폰, 역할, 발행시간, 종료시간, 암호화키, 를 압축하겠다

//    public Authentication getAuthenticationByRefreshToken(String token){
//        Long id = getId(token);
//        CustomUserDetails customUserDetails = userService.loadUserById(id);
//
//        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//    }

}
