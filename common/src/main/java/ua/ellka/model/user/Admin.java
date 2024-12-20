package ua.ellka.model.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class Admin extends User {

    private boolean superAdmin;

}
