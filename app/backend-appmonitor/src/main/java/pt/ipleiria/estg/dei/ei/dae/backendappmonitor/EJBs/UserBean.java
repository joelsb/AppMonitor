package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.NewPasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;
    @EJB
    private XLSXFileBean xlsxFileBean;


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

    public User changePassword(String username, String currentPassword, String newPassword, String newPasswordConfirmation) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var user = this.find(username);
        var currentPasswordHash = hasher.hash(currentPassword);
        if(!currentPasswordHash.equals(user.getPassword())) {
            throw new MyIllegalArgumentException("Current password is incorrect");
        }
        if(newPassword.equals(currentPassword)) {
            throw new MyIllegalArgumentException("New password must be different from the current password");
        }
        if(!newPassword.equals(newPasswordConfirmation)) {
            throw new MyIllegalArgumentException("New password and confirmation must match");
        }
        user.setPassword(hasher.hash(newPassword));
        xlsxFileBean.saveAllUsersToXlsx();
        return user;
    }
}
