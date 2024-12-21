package ua.ellka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private int priority;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private String assignedEmployee;
}
