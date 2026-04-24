package pl.products.management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.products.management.model.api.request.LoginRequest;
import pl.products.management.model.api.response.LoginResponse;
import pl.products.management.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> generateToken(@RequestBody @Valid LoginRequest request){

        String gotAccessToken = authService.login(request.username(), request.password());
        LoginResponse response = new LoginResponse(gotAccessToken);

        return ResponseEntity.ok(response);
    }
}
