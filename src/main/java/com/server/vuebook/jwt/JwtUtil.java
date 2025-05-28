package com.server.vuebook.jwt;

import com.server.vuebook.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;


@Slf4j
@Service
public class JwtUtil {

    private static final String SECRET = "this-is-a-very-long-and-secure-key-which-has-more-than-64-bytes-in-length!!!!!!";
    private final SecretKey secreKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));


    public String createToken (Member vo) {
        return Jwts.builder()
                .signWith(secreKey)
                .setClaims(Map.of(
                        "memNo", vo.getMemNo(),
                        "memName", vo.getMemName()
                ))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .compact();
    }

    public Member validate(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secreKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Member.builder()
                .memNo((int)claims.get("memNo"))
                .memName((String)claims.get("memName"))
                .build();
    }
}
