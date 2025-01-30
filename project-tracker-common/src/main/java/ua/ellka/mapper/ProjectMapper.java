package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.model.project.Project;
import ua.ellka.model.project.ProjectStatus;
import ua.ellka.model.user.Manager;

@Mapper (unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(source = "status.status", target = "status")
    @Mapping(source = "manager.nickname", target = "managerName")
    ProjectDTO projectToProjectDTO(Project project);

    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "managerName", target = "manager", qualifiedByName = "mapManager")
    Project projectDTOToProject(ProjectDTO DTO);

    @Named("mapStatus")
    default ProjectStatus mapStatus(String status) {
        return ProjectStatus.fromString(status);
    }

    @Named("mapManager")
    default Manager mapManager(String managerName) {
        if (managerName == null) {
            return null;
        }
        Manager manager = new Manager();
        manager.setNickname(managerName);
        return manager;
    }

}
