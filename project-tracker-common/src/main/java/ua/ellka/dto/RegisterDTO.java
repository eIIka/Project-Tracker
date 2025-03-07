package ua.ellka.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String nickname;
    private String firstName;
    private String lastName;
    private String role;
    private String password;
}
