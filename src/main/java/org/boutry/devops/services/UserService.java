package org.boutry.devops.services;

import org.boutry.devops.models.User;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.util.*;

@ApplicationScoped
public class UserService {

    private HashMap<Long, User> users;

    public UserService() {
        users = new HashMap<>();
        users.put(1L, new User(1, "Patrick", "Balkany"));
        users.put(2L, new User(2, "Jean", "Valjean"));
        users.put(3L, new User(3, "David", "Lafarge"));
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUser(long id) throws NotFoundException {
        return users.get(id);
    }


    public void addUser(User user) {
        users.put(user.id, user);
    }

    public void deleteUser(User user) {
        users.remove(user.id, user);
    }
}
