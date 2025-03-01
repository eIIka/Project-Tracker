package ua.ellka.controller;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.exception.NotFoundServiceException;
import ua.ellka.exception.ServiceException;
import ua.ellka.service.ProjectService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends ControllerTestParent {
    @Mock
    private ProjectService projectService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        ProjectController projectController = new ProjectController(projectService);

        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void getAllProjectsByUserId_returnsOk() throws Exception {
        when(projectService.getAllProjectsByUserId(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/v1/project?userId=1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getAllProjectsByUserId_returnsNotFound() throws Exception {
        when(projectService.getAllProjectsByUserId(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/project?userId=999"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getProjectById_returnsOk() throws Exception {
        when(projectService.getProject(1L)).thenReturn(new ProjectDTO());

        mockMvc.perform(get("/v1/project/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getProjectById_returnsNotFound() throws Exception {
        when(projectService.getProject(1L)).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/project/1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void createProject_returnsOk() throws Exception {
        when(projectService.createProject(any())).thenReturn(new ProjectDTO());

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                post("/v1/project")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createProject_returnsNotFound() throws Exception {
        when(projectService.createProject(any())).thenThrow(NotFoundServiceException.class);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                post("/v1/project")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createProject_returnsInternalServerError() throws Exception {
        when(projectService.createProject(any())).thenThrow(PersistenceException.class);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                post("/v1/project")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isInternalServerError(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createProject_returnsConflict() throws Exception {
        when(projectService.createProject(any())).thenThrow(ServiceException.class);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                post("/v1/project")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void updateProject_returnsOk() throws Exception {
        when(projectService.updateProject(anyLong(), any())).thenReturn(new ProjectDTO());

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                put("/v1/project/1")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void updateProject_returnsNotFound() throws Exception {
        when(projectService.updateProject(anyLong(), any())).thenThrow(NotFoundServiceException.class);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                put("/v1/project/1")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void updateProject_returnsConflict() throws Exception {
        when(projectService.updateProject(anyLong(), any())).thenThrow(ServiceException.class);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setPriority(10);

        String reqBody = objectMapper.writeValueAsString(projectDTO);
        mockMvc.perform(
                put("/v1/project/1")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void assignUserToProject_returnsOk() throws Exception {
        when(projectService.assignUserToProject(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/v1/project/3/assignUser?userId=1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void assignUserToProject_returnsNotFound() throws Exception {
        when(projectService.assignUserToProject(anyLong(), anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(put("/v1/project/3/assignUser?userId=1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void assignUserToProject_returnsConflict() throws Exception {
        when(projectService.assignUserToProject(anyLong(), anyLong())).thenThrow(ServiceException.class);

        mockMvc.perform(put("/v1/project/3/assignUser?userId=1"))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteProject_returnsOk() throws Exception {
        when(projectService.deleteProject(anyLong())).thenReturn(new ProjectDTO());

        mockMvc.perform(delete("/v1/project/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteProject_returnsNotFound() throws Exception {
        when(projectService.deleteProject(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(delete("/v1/project/1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteProject_returnsConflict() throws Exception {
        when(projectService.deleteProject(anyLong())).thenThrow(ServiceException.class);

        mockMvc.perform(delete("/v1/project/1"))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void removeProject_returnsOk() throws Exception {
        when(projectService.removeUserFromProject(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(delete("/v1/project/1/removeUser?userId=1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void removeProject_returnsNotFound() throws Exception {
        when(projectService.removeUserFromProject(anyLong(), anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(delete("/v1/project/1/removeUser?userId=1"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void removeProject_returnsConflict() throws Exception {
        when(projectService.removeUserFromProject(anyLong(), anyLong())).thenThrow(ServiceException.class);

        mockMvc.perform(delete("/v1/project/1/removeUser?userId=1"))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }
}