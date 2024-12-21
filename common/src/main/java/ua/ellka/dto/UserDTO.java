package ua.ellka.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
}
