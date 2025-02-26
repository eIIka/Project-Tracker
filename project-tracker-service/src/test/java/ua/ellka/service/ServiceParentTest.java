package ua.ellka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ellka.config.ServiceTestConfig;
import ua.ellka.mapper.ProjectMapper;
import ua.ellka.mapper.TaskMapper;
import ua.ellka.mapper.UserMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class ServiceParentTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProjectMapper projectMapper;

    @Autowired
    protected TaskMapper taskMapper;

    @Autowired
    protected UserMapper userMapper;
}
