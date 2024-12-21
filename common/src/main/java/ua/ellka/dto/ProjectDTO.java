package ua.ellka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private int priority;
    private String status;
    private LocalDateTime createdAt;
    private String managerName;
}
