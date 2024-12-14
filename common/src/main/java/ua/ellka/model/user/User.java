package ua.ellka.model.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
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

    public User(Long id, String name, String email, String password, String phoneNumber,
                LocalDateTime registeredAt, LocalDateTime lastLoginAt, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registeredAt = registeredAt;
        this.lastLoginAt = lastLoginAt;
        this.role = role;
    }

    public void validateRole(UserRole requiredRole) {
        if (!this.getRole().equals(requiredRole)) {
            throw new SecurityException("User does not have the required role: " + requiredRole);
        }
    }
}
