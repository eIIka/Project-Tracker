package ua.ellka.repo;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.Employee;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;

import java.util.Properties;

public class RepoParent {
    protected EntityManagerFactory entityManagerFactory;
    protected SessionFactory sessionFactory;

    @BeforeEach
    public void setUpParent() {
        createEntityManagerFactory();
        createSessionFactory();
    }

    @AfterEach
    public void tearDownParent() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private void createEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("project-tracker-test-pu");
    }

    private void createSessionFactory() {
        Configuration configuration = new Configuration();
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.h2.Driver");
        props.put("hibernate.connection.url", "jdbc:h2:mem:test_hibernate;INIT=runscript from 'classpath:init.sql'");
        props.put("hibernate.current_session_context_class", "thread");

        configuration.setProperties(props);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Manager.class);
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(Project.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
}
