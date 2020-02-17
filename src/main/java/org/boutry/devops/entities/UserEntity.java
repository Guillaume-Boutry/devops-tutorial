package org.boutry.devops.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.boutry.devops.models.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Entity
public class UserEntity extends PanacheEntity {

    @NotNull(message = "Firstname must not be null")
    @NotBlank(message = "Firstname must not be blank")
    String firstname;
    @NotNull(message = "Lastname must not be null")
    @NotBlank(message = "Lastname must not be blank")
    String lastname;
    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Email
    @Column(unique = true)
    String email;

    public static UserEntity fromUser(User user) {
        UserEntity uE = new UserEntity();
        uE.firstname = user.getFirstname();
        uE.lastname = user.getLastname();
        uE.email = user.getEmail();
        return uE;
    }

    public static UserEntity findById(long id) {
        return find("id", id).firstResult();
    }

    public static Optional<UserEntity> findByIdOptional(long id) {
        return Optional.ofNullable(UserEntity.findById(id));
    }

    public static Optional<UserEntity> findByEmailOptional(@NotNull String email) {
        return Optional.ofNullable(UserEntity.find("email", email).firstResult());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void persist() {
        Optional<UserEntity> user = UserEntity.findByEmailOptional(email);
        if (user.isPresent()) {
            throw new ConstraintViolationException("email should be unique", null);
        }
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
