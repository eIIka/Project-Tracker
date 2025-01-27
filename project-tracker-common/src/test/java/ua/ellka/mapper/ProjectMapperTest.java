package ua.ellka.mapper;

import org.junit.jupiter.api.Test;
import ua.ellka.dto.ProjectDTO;
import ua.ellka.model.project.Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectMapperTest {

    @Test
    void projectToProjectDTOTest() {
        ProjectDTO projectDTOTest = ProjectMapper.INSTANCE.projectToProjectDTO(TestData.PROJECT);

        assertNotNull(projectDTOTest);
        assertEquals(TestData.PROJECT.getId(), projectDTOTest.getId());
        assertEquals(TestData.PROJECT.getName(), projectDTOTest.getName());
        assertEquals(TestData.PROJECT.getDescription(), projectDTOTest.getDescription());
        assertEquals(TestData.PROJECT.getPriority(), projectDTOTest.getPriority());
        assertEquals(TestData.PROJECT.getStatus().getStatus(), projectDTOTest.getStatus());
        assertEquals(TestData.PROJECT.getCreatedAt(), projectDTOTest.getCreatedAt());
        assertEquals(TestData.PROJECT.getManager().getNickname(), projectDTOTest.getManagerName());
    }

    @Test
    void projectDTOToProjectTest() {
        Project projectTest = ProjectMapper.INSTANCE.projectDTOToProject(TestData.PROJECT_DTO);

        assertNotNull(projectTest);
        assertEquals(TestData.PROJECT_DTO.getId(), projectTest.getId());
        assertEquals(TestData.PROJECT_DTO.getName(), projectTest.getName());
        assertEquals(TestData.PROJECT_DTO.getDescription(), projectTest.getDescription());
        assertEquals(TestData.PROJECT_DTO.getPriority(), projectTest.getPriority());
        assertEquals(TestData.PROJECT_DTO.getStatus(), projectTest.getStatus().getStatus());
        assertEquals(TestData.PROJECT_DTO.getCreatedAt().toLocalDate().atStartOfDay(), projectTest.getCreatedAt());
        assertEquals(TestData.PROJECT_DTO.getManagerName(), projectTest.getManager().getNickname());
    }
}