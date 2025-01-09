package ua.ellka.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    MANAGER ("Manager"),
    EMPLOYEE ("Employee");

    private final String role;

    public static UserRole fromString(String role) {
        for (UserRole u : UserRole.values()) {
            if (u.getRole().equalsIgnoreCase(role)) {
                return u;
            }
        }
        return UserRole.EMPLOYEE;
    }
}
