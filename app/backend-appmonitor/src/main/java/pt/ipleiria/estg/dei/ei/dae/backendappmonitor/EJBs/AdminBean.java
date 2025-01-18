package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Admin;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.util.List;

@Stateless
public class AdminBean {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private UserBean userBean;
    @Inject
    private Hasher hasher;
    @EJB
    private XLSXFileBean xlsxFileBean;

    public Admin find(String username) throws MyEntityNotFoundException {
        var admin = entityManager.find(Admin.class, username);
        if (admin == null) {
            throw new MyEntityNotFoundException("User with username: '" + username + "' not found");
        }
        return admin;
    }

    public List<Admin> findAll() {
        return entityManager.createNamedQuery("getAllAdmins", Admin.class).getResultList();
    }

    public Admin create(String username, String password, String name, String email) throws MyIllegalArgumentException, MyEntityExistsException {
        if(entityManager.find(User.class, username) != null) {
            throw new MyEntityExistsException("User with username: '" + username + "' already exists");
        }

        userBean.validateFieldsCreate(username, password, name, email);
        var admin = new Admin(
                username, hasher.hash(password),name, email);
        entityManager.persist(admin);

        xlsxFileBean.saveAllUsersToXlsx();
        return admin;
    }

    public Admin update(String username, String name, String email) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var admin = this.find(username);
        entityManager.lock(admin, LockModeType.OPTIMISTIC);

        userBean.update(username, name, email);

        xlsxFileBean.saveAllUsersToXlsx();
        return admin;
    }

}
