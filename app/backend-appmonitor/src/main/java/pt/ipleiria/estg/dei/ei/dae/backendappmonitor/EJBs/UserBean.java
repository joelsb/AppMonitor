package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager entityManager;


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
}
