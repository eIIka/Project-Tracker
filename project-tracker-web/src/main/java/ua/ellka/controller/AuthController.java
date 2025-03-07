package ua.ellka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ellka.dto.LoginDTO;
import ua.ellka.dto.RegisterDTO;
import ua.ellka.dto.UserDTO;
import ua.ellka.service.AuthService;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO) {
        UserDTO userDTO = authService.registerUser(registerDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<UserDTO> logout() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }
}
