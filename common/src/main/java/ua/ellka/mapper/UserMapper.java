package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ellka.dto.AdminDTO;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.model.user.Admin;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AdminDTO toAdminDTO(Admin admin);
    Admin toAdmin(AdminDTO adminDTO);

    EmployeeDTO toEmployeeDTO(Employee employee);
    Employee toEmployee(EmployeeDTO employeeDTO);

    ManagerDTO toManagerDTO(Manager manager);
    Manager toManager(ManagerDTO managerDTO);
}
