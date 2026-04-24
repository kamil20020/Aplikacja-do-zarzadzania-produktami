package pl.products.management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.products.management.mapper.UserMapper;
import pl.products.management.model.api.request.RegisterRequest;
import pl.products.management.model.entity.UserEntity;
import pl.products.management.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request){

        UserEntity toCreateUserData = userMapper.map(request);
        userService.register(toCreateUserData);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
