package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Manager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.util.List;

@Stateless
public class ManagerBean extends UserBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private XLSXFileBean xlsxFileBean;
    @EJB
    private UserBean userBean;
    @Inject
    private Hasher hasher;

    public Manager find(String username) {
        var manager = entityManager.find(Manager.class, username);
        if (manager == null) {
            throw new RuntimeException("Manager with username: '" + username + "' not found");
        }
        return manager;
    }
    public List<Manager> findAllManagers() {
        var managers = entityManager.createNamedQuery("getAllManagers", Manager.class).getResultList();
        if(managers.isEmpty()) {
            throw new MyEntityNotFoundException("No managers found");
        }
        return managers;
    }
    @Transactional
    public Manager create(String username, String password, String name, String email, String office) throws MyEntityExistsException, MyIllegalArgumentException {
        if(entityManager.find(User.class, username) != null) {
            throw new MyEntityExistsException("User with username: '" + username + "' already exists");
        }

        userBean.validateFieldsCreate(username, password, name, email);
        if(office == null || office.isEmpty()) throw new MyIllegalArgumentException("Office cannot be empty");
        var manager = new Manager(
                username, hasher.hash(password), name, email, office);
        entityManager.persist(manager);

        xlsxFileBean.saveAllUsersToXlsx();
        return manager;
    }
    @Transactional
    public Manager update(String username, String name, String email, String office) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var manager = this.find(username);
        entityManager.lock(manager, LockModeType.OPTIMISTIC);

        if(office != null) manager.setOffice(office);
        userBean.update(username, name, email);

        xlsxFileBean.saveAllUsersToXlsx();
        return manager;
    }
}
