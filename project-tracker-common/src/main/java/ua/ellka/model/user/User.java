package ua.ellka.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    private Long id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private UserRole role;

    private LocalDateTime registeredAt;
    private LocalDateTime lastLoginAt;
}
