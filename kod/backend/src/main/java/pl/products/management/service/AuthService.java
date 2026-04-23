package pl.products.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import pl.products.management.model.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String login(String username, String password) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            username,
            password
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        return jwtService.generateAccessToken(loggedUser);
    }
}
