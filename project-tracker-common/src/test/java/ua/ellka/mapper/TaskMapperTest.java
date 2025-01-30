package ua.ellka.mapper;

import org.junit.jupiter.api.Test;
import ua.ellka.dto.TaskDTO;
import ua.ellka.model.task.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    @Test
    void taskToTaskDTOTest() {
        TaskDTO taskDTOTest = TaskMapper.INSTANCE.taskToTaskDTO(TestData.TASK);

        assertNotNull(taskDTOTest);
        assertEquals(TestData.TASK.getId(), taskDTOTest.getId());
        assertEquals(TestData.TASK.getName(), taskDTOTest.getName());
        assertEquals(TestData.TASK.getDescription(), taskDTOTest.getDescription());
        assertEquals(TestData.TASK.getStatus().getStatus(), taskDTOTest.getStatus());
        assertEquals(TestData.TASK.getType(), taskDTOTest.getType());
        assertEquals(TestData.TASK.getPriority(), taskDTOTest.getPriority());
        assertEquals(TestData.TASK.getCreatedAt(), taskDTOTest.getCreatedAt());
        assertEquals(TestData.TASK.getDeadline(), taskDTOTest.getDeadline());
        assertEquals(TestData.TASK.getManager().getNickname(), taskDTOTest.getAssignedManager());
        assertEquals(TestData.TASK.getEmployee().getNickname(), taskDTOTest.getAssignedEmployee());
        assertEquals(TestData.TASK.getProject().getName(), taskDTOTest.getProjectName());
    }

    @Test
    void taskDTOToTaskTest() {
        Task taskTest = TaskMapper.INSTANCE.taskDTOToTask(TestData.TASK_DTO);

        assertNotNull(taskTest);
        assertEquals(TestData.TASK_DTO.getId(), taskTest.getId());
        assertEquals(TestData.TASK_DTO.getName(), taskTest.getName());
        assertEquals(TestData.TASK_DTO.getDescription(), taskTest.getDescription());
        assertEquals(TestData.TASK_DTO.getStatus(), taskTest.getStatus().getStatus());
        assertEquals(TestData.TASK_DTO.getType(), taskTest.getType());
        assertEquals(TestData.TASK_DTO.getPriority(), taskTest.getPriority());
        assertEquals(TestData.TASK_DTO.getCreatedAt().toLocalDate().atStartOfDay(), taskTest.getCreatedAt().toLocalDate().atStartOfDay());
        assertEquals(TestData.TASK_DTO.getDeadline(), taskTest.getDeadline());
        assertEquals(TestData.TASK_DTO.getAssignedManager(), taskTest.getManager().getNickname());
        assertEquals(TestData.TASK_DTO.getAssignedEmployee(), taskTest.getEmployee().getNickname());
        assertEquals(TestData.TASK_DTO.getProjectName(), taskTest.getProject().getName());
    }
}