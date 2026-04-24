package pl.products.management.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        Optional<Authentication> authenticationOpt = getAuthenticationData(gotToken);

        if(authenticationOpt.isPresent()){

            SecurityContextHolder.getContext().setAuthentication(authenticationOpt.get());
        }

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

    private Optional<Authentication> getAuthenticationData(String gotToken){

        String gotUsername;

        try{
            gotUsername = jwtService.extractUsername(gotToken);
        }
        catch(JwtException e){

            log.error(e.getMessage());

            return Optional.empty();
        }

        UserDetails foundUser = userDetailsService.loadUserByUsername(gotUsername);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            foundUser,
            null,
            foundUser.getAuthorities()
        );

        return Optional.of(token);
    }
}
