package ua.ellka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String type;
    private int priority;
    private LocalDateTime createdAt;
    private LocalDate deadline;
    private String assignedManager;
    private String assignedEmployee;
    private String projectName;
}
