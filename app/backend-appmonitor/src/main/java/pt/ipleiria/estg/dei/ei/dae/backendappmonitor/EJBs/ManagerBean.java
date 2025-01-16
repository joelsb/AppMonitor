package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ManagerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Manager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ManagerBean extends UserBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private XLSXFileBean xlsxFileBean;

    public Manager find(String username) {
        var manager = entityManager.find(Manager.class, username);
        if (manager == null) {
            throw new RuntimeException("Manager with username: '" + username + "' not found");
        }
        return manager;
    }

    public List<Manager> findAll(String username) throws MyEntityNotFoundException {
        // remember, maps to: “SELECT a FROM User a ORDER BY a.name”
        return entityManager.createNamedQuery("getAllManagers", Manager.class).getResultList();
    }
    @Transactional
    public Manager create(String username, String password, String name, String email, String office) throws MyEntityExistsException {
        if(entityManager.find(Manager.class, username) != null) {
            throw new MyEntityExistsException("Manager with username: '" + username + "' already exists");
        }
        var manager = new Manager(
                username, password, name, email, office);
        entityManager.persist(manager);
        xlsxFileBean.saveAllUsersToXlsx();

        return manager;
    }
    @Transactional
    public Manager update(String username, String name, String email, String office) throws MyEntityNotFoundException {
        var manager = entityManager.find(Manager.class, username);
        if (manager == null) {
            throw new MyEntityNotFoundException("Manager with username: '" + username + "' not found");
        }
        entityManager.lock(manager, LockModeType.OPTIMISTIC);
        manager.setName(name);
        manager.setEmail(email);
        manager.setOffice(office);
        entityManager.persist(manager);
        xlsxFileBean.saveAllUsersToXlsx();
        return manager;
    }
}
