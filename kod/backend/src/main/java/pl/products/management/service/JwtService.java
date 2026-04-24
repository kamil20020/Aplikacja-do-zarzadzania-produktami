package pl.products.management.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.products.management.mapper.DateMapper;
import pl.products.management.model.entity.UserEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.access_token.expiration.duration}")
    private Integer accessTokenExpiration;

    @Value("${jwt.secret}")
    private String secret;

    private final DateMapper dateMapper;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateAccessToken(UserEntity userEntity){

        UUID tokenId = UUID.randomUUID();

        LocalDateTime rawIssuedAt = LocalDateTime.now();
        Date issuedAt = dateMapper.map(rawIssuedAt);

        LocalDateTime rawExpirationDate = rawIssuedAt.plusSeconds(accessTokenExpiration);
        Date expirationDate = dateMapper.map(rawExpirationDate);

        return Jwts.builder()
            .id(tokenId.toString())
            .setSubject(userEntity.getUsername())
            .setIssuer(issuer)
            .setAudience(audience)
            .setIssuedAt(issuedAt)
            .setExpiration(expirationDate)
            .claim("firstname", userEntity.getFirstname())
            .claim("surname", userEntity.getLastname())
            .claim("roles", userEntity.getRoles())
            .signWith(getSecretKey(), SIGNATURE_ALGORITHM)
            .compact();
    }

    public String extractUsername(String token) throws JwtException{

        Claims claims = validateToken(token)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    private JwtParser validateToken(String token) throws JwtException{

        if(token == null || token.isBlank()){
            throw new JwtException("Jwt token was not given");
        }

        return Jwts.parser()
            .verifyWith(getSecretKey())
            .requireIssuer(issuer)
            .requireAudience(audience)
            .build();
    }

    private SecretKey getSecretKey(){

        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(secretBytes);
    }
}

