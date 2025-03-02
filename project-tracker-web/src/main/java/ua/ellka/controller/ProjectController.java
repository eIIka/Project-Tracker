package ua.ellka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.service.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjectsByUserId(@RequestParam(name = "userId") Long userId) {
        List<ProjectDTO> getAllProjectsByUserId = projectService.getAllProjectsByUserId(userId);

        return ResponseEntity.ok(getAllProjectsByUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable(name = "id") Long id) {
        ProjectDTO projectDTO = projectService.getProject(id);

        return ResponseEntity.ok(projectDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO created = projectService.createProject(projectDTO);

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable("id") Long id,
                                                    @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updated = projectService.updateProject(id, projectDTO);

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{projectId}/assignUser")
    public Boolean assignUserToProject(@PathVariable("projectId") Long projectId,
                                                          @RequestParam(name = "userId") Long userId) {

        return projectService.assignUserToProject(projectId, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectDTO> deleteProject(@PathVariable("id") Long id) {
        ProjectDTO deleted = projectService.deleteProject(id);

        return ResponseEntity.ok(deleted);
    }

    @DeleteMapping("/{projectId}/removeUser")
    public Boolean removeUserFromProject(@PathVariable("projectId") Long projectId,
                                                       @RequestParam(name = "userId") Long userId) {

        return projectService.removeUserFromProject(projectId, userId);
    }
}
