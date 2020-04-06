package org.boutry.devops.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

@Entity
public class CatEntity extends PanacheEntity implements Serializable {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be null")
    public String name;

    @ManyToOne
    public UserEntity owner;

    public static CatEntity findById(long id) {
        return find("id", id).firstResult();
    }

    public static Optional<CatEntity> findByIdOptional(long id) {
        return Optional.ofNullable(CatEntity.findById(id));
    }

    @Override
    public String toString() {
        return "CatEntity{" +
                "name='" + name + '\'' +
                ", owner=" + owner +
                ", id=" + id +
                '}';
    }
}
