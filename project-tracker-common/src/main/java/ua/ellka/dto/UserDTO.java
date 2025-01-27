package ua.ellka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDTO {
    private Long id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
}
