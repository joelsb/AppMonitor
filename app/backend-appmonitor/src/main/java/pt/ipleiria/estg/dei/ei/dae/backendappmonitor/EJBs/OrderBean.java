package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class OrderBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Order create(long id, Date createdDate, String customerUsername, List<Volume> volumes) throws MyEntityExistsException, MyEntityNotFoundException {
        //Deal when order already exists
        var order = entityManager.find(Order.class, id);
        if(order != null) {
            order=addVolumes(id, volumes);
        }
        //Deal when order does not exist
        else {
            var customer = entityManager.find(Customer.class, customerUsername);
            order = new Order(
                    id, createdDate, null,customer);
            customer.addOrder(order);
            //TODO: CREATE VOLUMES AND ASSOCIATE THEM TO THE ORDER
            entityManager.persist(order);
            return order;
        }
        return order;
    }

    public Order updateDeliveredDate(long id, Date deliveredDate) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if(order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        order.setDeliveredDate(deliveredDate);
        return order;
    }

    public Order addVolumes(long id, List<Volume> volumes) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if(order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        for (Volume volume : volumes) {
            order.addVolume(volume);
            volume.setOrder(order);
        }
        return order;
    }

    public Order find(long id) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if(order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        return order;
    }

    public List<Order> findAll() {
        return entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
    }
}
