package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.UserRole;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "taskCount", expression = "java(employee.getTasks() != null ? (long) employee.getTasks().size() : 0L)")
    @Mapping(target = "projectCount", expression = "java(employee.getProjects() != null ? (long) employee.getProjects().size() : 0L)")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    @Mapping(source = "role", target = "role", qualifiedByName = "mapStatus")
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    @Mapping(target = "projectCount", expression = "java(manager.getProjects() != null ? (long) manager.getProjects().size() : 0L)")
    ManagerDTO managerToManagerDTO(Manager manager);

    @Mapping(source = "role", target = "role", qualifiedByName = "mapStatus")
    Manager managerDTOToManager(ManagerDTO managerDTO);

    @Named("mapStatus")
    default UserRole mapStatus(String role) {
        return UserRole.fromString(role);
    }

}
