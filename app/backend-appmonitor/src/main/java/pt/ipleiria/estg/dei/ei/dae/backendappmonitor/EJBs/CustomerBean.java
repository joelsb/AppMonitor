package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.util.List;


@Stateless
public class CustomerBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private UserBean userBean;
    @Inject
    private Hasher hasher;

    @EJB
    private XLSXFileBean xlsxFileBean;

    public Customer find(String username) throws MyEntityNotFoundException {
        var customer = entityManager.find(Customer.class, username);
        if (customer == null) {
            throw new MyEntityNotFoundException("User with username: '" + username + "' not found");
        }
        return customer;
    }

    public List<Customer> findAllCustomers() {
        var customers = entityManager.createNamedQuery("getAllCustomers", Customer.class).getResultList();
        if(customers.isEmpty()) {
            throw new MyEntityNotFoundException("No customers found");
        }
        // remember, maps to: “SELECT a FROM User a ORDER BY a.name”
        return customers;
    }

    public Customer findWithOrders(String username) throws MyEntityNotFoundException {
        var customer = this.find(username);
        //Initialize the lazy collection
        Hibernate.initialize(customer.getOrders());
        var orders = customer.getOrders();
        for(Order order : orders) {
            Hibernate.initialize(order.getVolumes());
        }
        return customer;
    }

    public List<Customer> findAllWithOrders() {
        var customers = this.findAllCustomers();
        customers.forEach(customer -> Hibernate.initialize(customer.getOrders()));
        return customers;
    }

    @Transactional
    public Customer create(String username, String password, String name, String email) throws MyEntityExistsException, MyIllegalArgumentException {
        if(entityManager.find(User.class, username) != null) {
            throw new MyEntityExistsException("User with username: '" + username + "' already exists");
        }

        userBean.validateFieldsCreate(username, password, name, email);
        var customer = new Customer(
                username, hasher.hash(password),name, email);
        entityManager.persist(customer);

        xlsxFileBean.saveAllUsersToXlsx();
        return customer;
    }

    @Transactional
    public Customer update(String username, String name, String email) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var customer = this.find(username);
        entityManager.lock(customer, LockModeType.OPTIMISTIC);

        userBean.update(username, name, email);

        xlsxFileBean.saveAllUsersToXlsx();
        return customer;
    }
}
