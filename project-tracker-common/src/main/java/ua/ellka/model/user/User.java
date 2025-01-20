package ua.ellka.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
    private String password;

    @Column(name = "user_role", insertable = false, updatable = false)
    @Convert(converter = UserRoleConvertor.class)
    private UserRole role;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}
