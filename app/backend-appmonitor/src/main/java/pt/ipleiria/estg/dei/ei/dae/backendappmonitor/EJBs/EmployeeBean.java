package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.EmployeeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Employee;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Stateless
public class EmployeeBean extends UserBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Employee create(String username, String password, String name, String email, String warehouse) throws MyEntityExistsException {
        if(entityManager.find(Employee.class, username) != null) {
            throw new MyEntityExistsException("Employee (" + username + ") already exists");
        }
        var employee = new Employee(
                username, password, name, email, warehouse);
        entityManager.persist(employee);
        return employee;
    }

    public Employee update(String username, String name, String email, String Warehouse) throws MyEntityNotFoundException {
        var employee = entityManager.find(Employee.class, username);
        if (employee == null) {
            throw new MyEntityNotFoundException("Employee (" + username + ") not found");
        }
        employee.setName(name);
        employee.setEmail(email);
        employee.setWarehouse(Warehouse);
        return employee;
    }

    public Employee updatePassword(String username, String password) throws MyEntityNotFoundException {
        var employee = entityManager.find(Employee.class, username);
        if (employee == null) {
            throw new MyEntityNotFoundException("Employee (" + username + ") not found");
        }
        employee.setPassword(password);
        return employee;
    }

}
