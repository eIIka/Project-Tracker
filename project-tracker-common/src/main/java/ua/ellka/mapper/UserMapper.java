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

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default Long mapCount(Set<?> collection) {
        return collection != null ? (long) collection.size() : 0L;
    }

    @Mapping(target = "taskCount", expression = "java(mapCount(employee.getTasks()))")
    @Mapping(target = "projectCount", expression = "java(mapCount(employee.getProjects()))")
    @Mapping(target = "role", expression = "java(employee.getRole().getRole())")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    @Mapping(target = "projectCount", expression = "java(mapCount(manager.getProjects()))")
    @Mapping(target = "role", expression = "java(manager.getRole().getRole())")
    ManagerDTO managerToManagerDTO(Manager manager);

    Manager managerDTOToManager(ManagerDTO managerDTO);
}
