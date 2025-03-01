package ua.ellka.controller;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ellka.dto.TaskDTO;
import ua.ellka.exception.*;
import ua.ellka.service.TaskService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends ControllerTestParent {

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        TaskController taskController = new TaskController(taskService);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void getTaskTest_returnsOk() throws Exception {
        when(taskService.getTask(anyLong())).thenReturn(new TaskDTO());

        mockMvc.perform(get("/v1/task/3"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getTaskTest_returnsNotFound() throws Exception {
        when(taskService.getTask(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/task/3"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllTasksByUserIdTest_returnsOk() throws Exception {
        when(taskService.getAllTasksByUserId(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/v1/task?userId=5"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllTasksByUserIdTest_returnsNotFound() throws Exception {
        when(taskService.getAllTasksByUserId(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/task?userId=5"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllTasksByProjectIdTest_returnsOk() throws Exception {
        when(taskService.getAllTasksByProjectId(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/v1/task?projectId=5"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllTasksByProjectIdTest_returnsNotFound() throws Exception {
        when(taskService.getAllTasksByProjectId(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/task?projectId=5"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllTasksByProjectAndUserIdTest_returnsOk() throws Exception {
        when(taskService.getAllTasksByProjectIdAndUserId(anyLong(), anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/v1/task?projectId=5&userId=4"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllTasksByProjectAndUserIdTest_returnsNotFound() throws Exception {
        when(taskService.getAllTasksByProjectIdAndUserId(anyLong(), anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/task?projectId=5&userId=4"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void createTaskTest_returnsOk() throws Exception {
        when(taskService.createTask(any())).thenReturn(new TaskDTO());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                post("/v1/task")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createTaskTest_returnsNotFound() throws Exception {
        when(taskService.createTask(any())).thenThrow(NotFoundServiceException.class);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                post("/v1/task")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createTaskTest_returnsConflict() throws Exception {
        when(taskService.createTask(any())).thenThrow(ServiceException.class);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                post("/v1/task")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createTaskTest_returnsInternalServerError() throws Exception {
        when(taskService.createTask(any())).thenThrow(PersistenceException.class);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                post("/v1/task")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isInternalServerError(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void updateTaskTest_returnsOk() throws Exception {
        when(taskService.updateTask(anyLong(), any())).thenReturn(new TaskDTO());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                put("/v1/task/1")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void updateTaskTest_returnsNotFound() throws Exception {
        when(taskService.updateTask(anyLong(), any())).thenThrow(NotFoundServiceException.class);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                put("/v1/task/1")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void updateTaskTest_returnsConflict() throws Exception {
        when(taskService.updateTask(anyLong(), any())).thenThrow(ServiceException.class);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setDescription("description");
        taskDTO.setStatus("In progress");
        taskDTO.setPriority(5);

        String reqBody = objectMapper.writeValueAsString(taskDTO);
        mockMvc.perform(
                put("/v1/task/1")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void assignUserToTaskTest_returnsOk() throws Exception {
        when(taskService.assignUserToTask(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/v1/task/19/assignUser?userId=1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void assignUserToTaskTest_returnsNotFound() throws Exception {
        when(taskService.assignUserToTask(anyLong(), anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(put("/v1/task/19/assignUser?userId=1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void assignUserToTaskTest_returnsConflict() throws Exception {
        when(taskService.assignUserToTask(anyLong(), anyLong())).thenThrow(ServiceException.class);

        mockMvc.perform(put("/v1/task/19/assignUser?userId=1"))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteTaskTest_returnsOk() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn(new TaskDTO());

        mockMvc.perform(delete("/v1/task/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteTaskTest_returnsNotFound() throws Exception {
        when(taskService.deleteTask(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(delete("/v1/task/1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteTaskTest_returnsConflict() throws Exception {
        when(taskService.deleteTask(anyLong())).thenThrow(ServiceException.class);

        mockMvc.perform(delete("/v1/task/1"))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void removeUserFromTaskTest_returnsOk() throws Exception {
        when(taskService.removeUserFromTask(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(delete("/v1/task/19/removeUser?userId=1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void removeUserFromTaskTest_returnsNotFound() throws Exception {
        when(taskService.removeUserFromTask(anyLong(), anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(delete("/v1/task/19/removeUser?userId=1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void removeUserFromTaskTest_returnsConflict() throws Exception {
        when(taskService.removeUserFromTask(anyLong(), anyLong())).thenThrow(ServiceException.class);

        mockMvc.perform(delete("/v1/task/19/removeUser?userId=1"))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }
}