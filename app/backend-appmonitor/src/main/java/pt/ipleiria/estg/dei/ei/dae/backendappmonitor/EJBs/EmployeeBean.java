package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.EmployeeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Employee;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EmployeeBean extends UserBean {
    @PersistenceContext
    private EntityManager entityManager;

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

    public Employee create(String username, String password, String name, String email, String warehouse) throws MyEntityExistsException {
        if(entityManager.find(Employee.class, username) != null) {
            // add '' between the username in throw new MyEntityExistsException
            throw new MyEntityExistsException("Employee with username: '" + username + "' already exists");
        }
        var employee = new Employee(
                username, password, name, email, warehouse);
        entityManager.persist(employee);
        xlsxFileBean.saveAllUsersToXlsx();

        return employee;
    }

    public Employee update(String username, String name, String email, String Warehouse) throws MyEntityNotFoundException {
        var employee = entityManager.find(Employee.class, username);
        if (employee == null) {
            throw new MyEntityNotFoundException("Employee with username: '" + username + "' not found");
        }
        employee.setName(name);
        employee.setEmail(email);
        employee.setWarehouse(Warehouse);
        xlsxFileBean.saveAllUsersToXlsx();

        return employee;
    }

}
