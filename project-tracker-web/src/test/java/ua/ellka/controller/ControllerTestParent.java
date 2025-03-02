package ua.ellka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.ellka.config.WebTestConfig;
import ua.ellka.exception.GlobalExceptionHandler;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebTestConfig.class)
public class ControllerTestParent {
    protected MockMvc mockMvc;

    @Autowired
    protected GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;
}
