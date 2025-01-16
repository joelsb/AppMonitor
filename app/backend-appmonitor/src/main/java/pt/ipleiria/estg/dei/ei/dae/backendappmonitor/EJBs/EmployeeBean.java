package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Employee;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.util.List;

@Stateless
public class EmployeeBean extends UserBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;
    @EJB
    private UserBean userBean;
    @EJB
    private XLSXFileBean xlsxFileBean;

    public Employee find(String username) {
        var employee = entityManager.find(Employee.class, username);
        if (employee == null) {
            throw new RuntimeException("Employee with username: '" + username + "' not found");
        }
        return employee;
    }

    public List<Employee> findAll(String username) throws MyEntityNotFoundException {
        // remember, maps to: “SELECT a FROM User a ORDER BY a.name”
        return entityManager.createNamedQuery("getAllEmployees", Employee.class).getResultList();
    }

    public Employee create(String username, String password, String name, String email, String warehouse) throws MyEntityExistsException, MyIllegalArgumentException {
        if(entityManager.find(User.class, username) != null) {
            // add '' between the username in throw new MyEntityExistsException
            throw new MyEntityExistsException("User with username: '" + username + "' already exists");
        }

        userBean.validateFieldsCreate(username, password, name, email);
        if(warehouse == null || warehouse.isEmpty()) throw new MyIllegalArgumentException("Warehouse cannot be empty");
        var employee = new Employee(
                username, hasher.hash(password), name, email, warehouse);
        entityManager.persist(employee);

        xlsxFileBean.saveAllUsersToXlsx();
        return employee;
    }

    public Employee update(String username, String name, String email, String warehouse) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var employee = this.find(username);
        entityManager.lock(employee, LockModeType.OPTIMISTIC);

        if(warehouse != null) employee.setWarehouse(warehouse);
        if(warehouse != null && warehouse.isEmpty()) throw new MyIllegalArgumentException("Warehouse cannot be empty");
        userBean.update(username, name, email);

        xlsxFileBean.saveAllUsersToXlsx();
        return employee;
    }

}
