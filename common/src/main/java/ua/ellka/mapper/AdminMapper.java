package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ua.ellka.dto.AdminDTO;
import ua.ellka.model.user.Admin;
import ua.ellka.model.user.UserRole;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper extends UserMapper {
    @Mapping(target = "role", source = "role")
    AdminDTO adminToAdminDTO(Admin admin);

    @Mapping(target = "role", source = "role")
    default Admin adminDTOToAdmin(AdminDTO adminDTO){
        return Admin.builder()
                .id(adminDTO.getId())
                .name(adminDTO.getName())
                .email(adminDTO.getEmail())
                .role(UserRole.ADMIN)
                .superAdmin(adminDTO.isSuperAdmin())
                .build();
    }

}
