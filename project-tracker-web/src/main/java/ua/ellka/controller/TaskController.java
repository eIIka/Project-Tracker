package ua.ellka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.dto.TaskDTO;
import ua.ellka.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable(name = "id") Long id) {
        TaskDTO taskDTO = taskService.getTask(id);

        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<TaskDTO>> getAllTasksByUserId(@RequestParam(name = "userId") Long userId) {
        List<TaskDTO> getAllTasksByUserId = taskService.getAllTasksByUserId(userId);

        return ResponseEntity.ok(getAllTasksByUserId);
    }

    @GetMapping(params = "projectId")
    public ResponseEntity<List<TaskDTO>> getAllTasksByProjectId(@RequestParam(name = "projectId") Long projectId) {
        List<TaskDTO> getAllTasksByProjectId = taskService.getAllTasksByProjectId(projectId);

        return ResponseEntity.ok(getAllTasksByProjectId);
    }

    @GetMapping(params = {"projectId", "userId"})
    public ResponseEntity<List<TaskDTO>> getAllTasksByProjectAndUserId(@RequestParam(name = "projectId") Long projectId,
                                                                       @RequestParam(name = "userId") Long userId) {
        List<TaskDTO> getAllTasksByProjectId = taskService.getAllTasksByProjectIdAndUserId(projectId, userId);

        return ResponseEntity.ok(getAllTasksByProjectId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO task = taskService.createTask(taskDTO);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable("id") Long id,
                                                    @RequestBody TaskDTO taskDTO) {
        TaskDTO updated = taskService.updateTask(id, taskDTO);

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{taskId}/assignUser")
    public Boolean assignUserToTask(@PathVariable(name = "taskId") Long taskId,
                                                    @RequestParam(name = "userId") Long userId) {
        return taskService.assignUserToTask(taskId, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable("id") Long id) {
        TaskDTO deleted = taskService.deleteTask(id);

        return ResponseEntity.ok(deleted);
    }

    @DeleteMapping("/{taskId}/removeUser")
    public Boolean removeUserFromTask (@PathVariable(name = "taskId") Long taskId,
                                       @RequestParam(name = "userId") Long userId) {
        return taskService.removeUserFromTask(taskId, userId);
    }
}
