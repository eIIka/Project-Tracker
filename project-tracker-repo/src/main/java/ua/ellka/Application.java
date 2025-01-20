package ua.ellka;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ua.ellka.model.task.Task;

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
