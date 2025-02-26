package ua.ellka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ua.ellka.mapper.ProjectMapper;
import ua.ellka.mapper.TaskMapper;
import ua.ellka.mapper.UserMapper;

@Configuration
public class ServiceTestConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProjectMapper projectMapper() {
        return ProjectMapper.INSTANCE;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public TaskMapper taskMapper() {
        return TaskMapper.INSTANCE;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        return objectMapper;
    }
}
