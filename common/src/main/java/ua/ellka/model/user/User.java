package ua.ellka.model.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder

public abstract class User {
    private Long id;
    private String name;
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    private String password;
    private UserRole role;

    private LocalDateTime registeredAt;
    private LocalDateTime lastLoginAt;

}
