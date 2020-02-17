package org.boutry.devops.models;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class User {

    public long id;
    public String firstname;
    public String lastname;

    public User() {

    }

    public User(int id, @NotNull String firstname, @NotNull String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                firstname.equals(user.firstname) &&
                lastname.equals(user.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname);
    }
}
