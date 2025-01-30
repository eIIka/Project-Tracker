package ua.ellka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends UserDTO {
    private Long taskCount;
    private Long projectCount;

    @Override
    public String getRole() {
        return "Employee";
    }
}
