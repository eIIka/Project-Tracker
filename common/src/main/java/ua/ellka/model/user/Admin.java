package ua.ellka.model.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Admin extends User {

    public Admin(Long id, String name, String email, String password, String phoneNumber,
                   LocalDateTime registeredAt, LocalDateTime lastLoginAt) {
        super(id, name, email, password, phoneNumber, registeredAt, lastLoginAt, UserRole.ADMIN);
    }

}
