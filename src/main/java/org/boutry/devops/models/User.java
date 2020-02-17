package org.boutry.devops.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class User {
    @NotNull(message = "Firstname must not be null")
    @NotBlank(message = "Firstname must not be blank")
    private String firstname;
    @NotNull(message = "Lastname must not be null")
    @NotBlank(message = "Lastname must not be blank")
    private String lastname;
    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Email
    private String email;

    public User() {

    }

    public User(@NotNull String firstname, @NotNull String lastname, @NotNull String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
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

}
