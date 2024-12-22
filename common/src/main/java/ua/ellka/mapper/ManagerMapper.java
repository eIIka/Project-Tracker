package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.UserRole;

import java.util.HashSet;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManagerMapper extends UserMapper{

    @Mapping(target = "role", source = "role")
    ManagerDTO managerToManagerDTO(Manager manager);

    default Manager managerDTOToManager(ManagerDTO managerDTO){
        return Manager.builder()
                .id(managerDTO.getId())
                .name(managerDTO.getName())
                .email(managerDTO.getEmail())
                .role(UserRole.MANAGER)
                .projects(new HashSet<>(managerDTO.getProjectCount()))
                .build();
    }
}
