package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.UserRole;

import java.util.HashMap;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
    @Mapping(target = "role", source = "role")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    @Mapping(target = "role", source = "role")
    default Employee employeeDTOToEmployee(EmployeeDTO employeeDTO){
        return Employee.builder()
                .id(employeeDTO.getId())
                .name(employeeDTO.getName())
                .email(employeeDTO.getEmail())
                .role(UserRole.EMPLOYEE)
                .tasks(new HashMap<>(employeeDTO.getTaskCount()))
                .build();
    }

}
