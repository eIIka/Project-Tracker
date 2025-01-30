package ua.ellka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO extends UserDTO {
    private Long projectCount;

    @Override
    public String getRole() {
        return "Manager";
    }
}
