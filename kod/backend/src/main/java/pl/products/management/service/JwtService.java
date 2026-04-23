package pl.products.management.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.products.management.mapper.DateMapper;
import pl.products.management.model.entity.UserEntity;

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
    private static final String USERNAME_KEY = "username";

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
            .signWith(SIGNATURE_ALGORITHM, secret)
            .compact();
    }

    public String extractUsername(String token, JwtParser jwtParser){

        Claims claims = jwtParser
            .parseClaimsJws(token)
            .getBody();

        return (String) claims.get(USERNAME_KEY);
    }

    public JwtParser validateAccessToken(String token) throws JwtException{

        return validateToken(token, accessTokenExpiration);
    }

    private JwtParser validateToken(String token, Integer tokenExpiration) throws JwtException{

        if(token == null || token.isBlank()){
            throw new JwtException("Jwt token was not given");
        }

        LocalDateTime actualRawDate = LocalDateTime.now();
        Date actualDate = dateMapper.map(actualRawDate);

        LocalDateTime validExpirationRawDate = actualRawDate.plusSeconds(tokenExpiration);
        Date validExpirationDate = dateMapper.map(validExpirationRawDate);

        return Jwts.parser()
            .requireIssuer(issuer)
            .requireAudience(audience)
            .requireExpiration(validExpirationDate)
            .requireNotBefore(actualDate)
            .build();
    }
}

