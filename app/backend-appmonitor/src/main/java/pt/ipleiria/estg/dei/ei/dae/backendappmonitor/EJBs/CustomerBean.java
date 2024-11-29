package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Stateless
public class CustomerBean extends UserBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Customer findWithOrders(String username) throws MyEntityNotFoundException {
        var customer = entityManager.find(Customer.class, username);
        if (customer == null) {
            throw new MyEntityNotFoundException("Customer (" + username + ") not found");
        }
        return customer;
    }

    public Customer create(String username, String password, String name, String email) throws MyEntityExistsException {
        if(entityManager.find(Customer.class, username) != null) {
            throw new MyEntityExistsException("Customer (" + username + ") already exists");
        }
        var customer = new Customer(
                username, password, name, email);
        entityManager.persist(customer);
        return customer;
    }

    public Customer update(String username, String name, String email) throws MyEntityNotFoundException {
        var customer = entityManager.find(Customer.class, username);
        if (customer == null) {
            throw new MyEntityNotFoundException("Customer (" + username + ") not found");
        }
        customer.setName(name);
        customer.setEmail(email);
        return customer;
    }

    public Customer updatePassword(String username, String password) throws MyEntityNotFoundException {
        var customer = entityManager.find(Customer.class, username);
        if (customer == null) {
            throw new MyEntityNotFoundException("Customer (" + username + ") not found");
        }
        customer.setPassword(password);
        return customer;
    }
}
