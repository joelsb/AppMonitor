package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        //JPQL query to get all users
        @NamedQuery(name = "getAllCustomers", query = "SELECT c FROM Customer c ORDER BY c.username"),
        @NamedQuery(name = "getCustomerByEmail", query = "SELECT c FROM Customer c WHERE c.email = :email")
})

@Entity
public class Customer extends User implements Serializable {
    @NotNull
    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public List<Long> getOrdersIds() {
        List<Long> ordersIds = new ArrayList<>();
        for (Order order : orders) {
            ordersIds.add(order.getId());
        }
        return ordersIds;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
