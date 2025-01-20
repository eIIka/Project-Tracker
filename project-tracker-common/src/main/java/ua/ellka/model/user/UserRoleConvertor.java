package ua.ellka.model.user;

import jakarta.persistence.AttributeConverter;

public class UserRoleConvertor implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole role) {
        return role == null ? null : role.getRole();
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return UserRole.EMPLOYEE;
        }

        return UserRole.fromString(dbData);
    }
}
