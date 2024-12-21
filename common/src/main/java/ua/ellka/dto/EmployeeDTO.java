package ua.ellka.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmployeeDTO extends UserDTO {
    private int taskCount;
}
