package ua.ellka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ellka.dto.TaskDTO;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.task.TaskStatus;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;

@Mapper (unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "status.status", target = "status")
    @Mapping(source = "manager.nickname", target = "assignedManager")
    @Mapping(source = "employee.nickname", target = "assignedEmployee")
    @Mapping(source = "project.name", target = "projectName")
    TaskDTO taskToTaskDTO(Task task);

    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "assignedManager", target = "manager", qualifiedByName = "mapManager")
    @Mapping(source = "assignedEmployee", target = "employee", qualifiedByName = "mapEmployee")
    @Mapping(source = "projectName", target = "project", qualifiedByName = "mapProject")
    Task taskDTOToTask(TaskDTO taskDTO);

    @Named("mapStatus")
    default TaskStatus mapStatus(String status) {
        return TaskStatus.fromString(status);
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

    @Named("mapEmployee")
    default Employee mapEmployee(String employeeName) {
        if (employeeName == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setNickname(employeeName);
        return employee;
    }

    @Named("mapProject")
    default Project mapProject(String projectName) {
        if (projectName == null) {
            return null;
        }
        Project project = new Project();
        project.setName(projectName);
        return project;
    }
}
