package ua.ellka.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import ua.ellka.model.task.Task;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String phoneNumber;
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private UserRole role;
    private LocalDateTime registeredAt;
    private LocalDateTime lastLoginAt;
    private Set<Task> tasks = new HashSet<>();
}
