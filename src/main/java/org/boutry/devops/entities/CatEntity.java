package org.boutry.devops.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class CatEntity extends PanacheEntity implements Serializable {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be null")
    public String name;

    @ManyToOne
    public UserEntity owner;

    @Override
    public String toString() {
        return "CatEntity{" +
                "name='" + name + '\'' +
                ", owner=" + owner +
                ", id=" + id +
                '}';
    }
}
