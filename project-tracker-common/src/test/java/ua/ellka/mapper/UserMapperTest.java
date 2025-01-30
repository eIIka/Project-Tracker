package ua.ellka.mapper;

import org.junit.jupiter.api.Test;
import ua.ellka.dto.EmployeeDTO;
import ua.ellka.dto.ManagerDTO;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void employeeToEmployeeDTOTest() {
        EmployeeDTO employeeDTOTest = UserMapper.INSTANCE.employeeToEmployeeDTO(TestData.EMPLOYEE);

        assertNotNull(employeeDTOTest);
        assertEquals(TestData.EMPLOYEE.getId(), employeeDTOTest.getId());
        assertEquals(TestData.EMPLOYEE.getNickname(), employeeDTOTest.getNickname());
        assertEquals(TestData.EMPLOYEE.getFirstName(), employeeDTOTest.getFirstName());
        assertEquals(TestData.EMPLOYEE.getLastName(), employeeDTOTest.getLastName());
        assertEquals(TestData.EMPLOYEE.getEmail(), employeeDTOTest.getEmail());
        assertEquals(TestData.EMPLOYEE.getPhoneNumber(), employeeDTOTest.getPhoneNumber());
        assertEquals(TestData.EMPLOYEE.getRole().getRole(), employeeDTOTest.getRole());
        assertEquals(TestData.EMPLOYEE.getTasks().size(), employeeDTOTest.getTaskCount());
        assertEquals(TestData.EMPLOYEE.getProjects().size(), employeeDTOTest.getProjectCount());
    }

    @Test
    void employeeDTOToEmployeeTest() {
        Employee employeeTest = UserMapper.INSTANCE.employeeDTOToEmployee(TestData.EMPLOYEE_DTO);

        assertNotNull(employeeTest);
        assertEquals(TestData.EMPLOYEE_DTO.getId(), employeeTest.getId());
        assertEquals(TestData.EMPLOYEE_DTO.getNickname(), employeeTest.getNickname());
        assertEquals(TestData.EMPLOYEE_DTO.getFirstName(), employeeTest.getFirstName());
        assertEquals(TestData.EMPLOYEE_DTO.getLastName(), employeeTest.getLastName());
        assertEquals(TestData.EMPLOYEE_DTO.getEmail(), employeeTest.getEmail());
        assertEquals(TestData.EMPLOYEE_DTO.getPhoneNumber(), employeeTest.getPhoneNumber());
        assertEquals(TestData.EMPLOYEE_DTO.getRole(), employeeTest.getRole().getRole());
        //assertEquals(TestData.EMPLOYEE_DTO.getTaskCount(), employeeTest.getTasks().size());
        //assertEquals(TestData.EMPLOYEE_DTO.getProjectCount(), employeeTest.getProjects().size());
    }

    @Test
    void managerToManagerDTOTest() {
        ManagerDTO managerDTOTest = UserMapper.INSTANCE.managerToManagerDTO(TestData.MANAGER);

        assertNotNull(managerDTOTest);
        assertEquals(TestData.MANAGER.getId(), managerDTOTest.getId());
        assertEquals(TestData.MANAGER.getNickname(), managerDTOTest.getNickname());
        assertEquals(TestData.MANAGER.getFirstName(), managerDTOTest.getFirstName());
        assertEquals(TestData.MANAGER.getLastName(), managerDTOTest.getLastName());
        assertEquals(TestData.MANAGER.getEmail(), managerDTOTest.getEmail());
        assertEquals(TestData.MANAGER.getPhoneNumber(), managerDTOTest.getPhoneNumber());
        assertEquals(TestData.MANAGER.getRole().getRole(), managerDTOTest.getRole());
        assertEquals(TestData.MANAGER.getProjects().size(), managerDTOTest.getProjectCount());
    }

    @Test
    void managerDTOToManagerTest() {
        Manager managerTest = UserMapper.INSTANCE.managerDTOToManager(TestData.MANAGER_DTO);

        assertNotNull(managerTest);
        assertEquals(TestData.MANAGER_DTO.getId(), managerTest.getId());
        assertEquals(TestData.MANAGER_DTO.getNickname(), managerTest.getNickname());
        assertEquals(TestData.MANAGER_DTO.getFirstName(), managerTest.getFirstName());
        assertEquals(TestData.MANAGER_DTO.getLastName(), managerTest.getLastName());
        assertEquals(TestData.MANAGER_DTO.getEmail(), managerTest.getEmail());
        assertEquals(TestData.MANAGER_DTO.getPhoneNumber(), managerTest.getPhoneNumber());
        assertEquals(TestData.MANAGER_DTO.getRole(), managerTest.getRole().getRole());
        //assertEquals(TestData.MANAGER_DTO.getProjectCount(), managerTest.getProjects().size());
    }
}