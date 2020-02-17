package org.boutry.devops.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.boutry.devops.models.User;

import javax.persistence.Entity;
import java.util.Objects;
import java.util.Optional;

@Entity
public class UserEntity extends PanacheEntity {
    public String firstname;
    public String lastname;

    public static UserEntity fromUser(User user) {
        UserEntity uE = new UserEntity();
        uE.firstname = user.getFirstname();
        uE.lastname = user.getLastname();
        return uE;
    }

    public static UserEntity findById(long id) {
        return find("id", id).firstResult();
    }

    public static Optional<UserEntity> findByIdOptional(long id) {
        return Optional.ofNullable(UserEntity.findById(id));
    }

    public static void delete(UserEntity user) {
        delete("id", user.id);
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public void persist() {
        super.persist();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return id.equals(user.id) &&
                firstname.equals(user.firstname) &&
                lastname.equals(user.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname);
    }

}
