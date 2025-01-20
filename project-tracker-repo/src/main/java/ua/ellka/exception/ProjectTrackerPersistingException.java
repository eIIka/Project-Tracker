package ua.ellka.exception;

/*
    Custom exception for persisting errors in the Project Tracker app
 */

public class ProjectTrackerPersistingException extends Exception {
    private final Object entity;

    public ProjectTrackerPersistingException(final Object entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }
}
