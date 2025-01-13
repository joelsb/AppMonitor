package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;


    public User find(String username) throws MyEntityNotFoundException {
        var user = entityManager.find(User.class, username);
        if (user == null) {
            throw new MyEntityNotFoundException("User with username: '" + username + "' not found");
        }
        return user;
    }
    public List<User> findAll() {
        // remember, maps to: “SELECT a FROM User a ORDER BY a.name”
        return entityManager.createNamedQuery("getAllUsers", User.class).getResultList();
    }
    public User findOrFail(String username) {
        var user = entityManager.getReference(User.class, username);
        Hibernate.initialize(user);
        return user;
    }

    public boolean canLogin(String username, String password) {
        var user = entityManager.find(User.class,username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    public User changePassword(String username, String password) throws MyEntityNotFoundException {
        var user = this.find(username);
        user.setPassword(hasher.hash(password));
        return user;
    }
}
