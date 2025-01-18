package ua.ellka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ua.ellka.model.project.Project;
import ua.ellka.model.task.Task;
import ua.ellka.model.user.Manager;
import ua.ellka.model.user.User;

public class Application {
    public static void main(String[] args) {

        try (
                EntityManagerFactory entityManagerFactory
                        = Persistence.createEntityManagerFactory("project-tracker-pu");

                EntityManager entityManager = entityManagerFactory.createEntityManager()
        ) {
            Task user = entityManager.find(Task.class, 5L);

            System.out.println();

        }
    }
}
