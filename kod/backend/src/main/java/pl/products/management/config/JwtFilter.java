package pl.products.management.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.products.management.service.JwtService;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        Optional<String> gotTokenOpt = extractToken(request);

        if(gotTokenOpt.isEmpty()){

            filterChain.doFilter(request, response);
            return;
        }

        String gotToken = gotTokenOpt.get();

        Authentication authentication = getAuthenticationData(gotToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request){

        String authorizationHeaderContent = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!hasToken(authorizationHeaderContent)){

           return Optional.empty();
        }

        String extractedToken = authorizationHeaderContent.substring(JWT_TOKEN_PREFIX.length());

        return Optional.of(extractedToken);
    }

    private boolean hasToken(String authContent){

        return authContent != null &&
               !authContent.isBlank() &&
               authContent.startsWith(JWT_TOKEN_PREFIX);
    }

    private Authentication getAuthenticationData(String gotToken) throws InvalidOneTimeTokenException{

        String gotUsername = extractUsername(gotToken);
        UserDetails foundUser = userDetailsService.loadUserByUsername(gotUsername);

        return new UsernamePasswordAuthenticationToken(
            foundUser,
            null,
            foundUser.getAuthorities()
        );
    }

    private String extractUsername(String gotToken) throws InvalidOneTimeTokenException{

        JwtParser jwtParser;

        try{
            jwtParser = jwtService.validateAccessToken(gotToken);
        }
        catch(JwtException exception) {
            throw new InvalidOneTimeTokenException("Jwt token could not be parsed");
        }

        return jwtService.extractUsername(gotToken, jwtParser);
    }
}
