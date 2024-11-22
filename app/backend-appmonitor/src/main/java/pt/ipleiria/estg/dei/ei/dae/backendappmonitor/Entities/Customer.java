package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public class Customer extends User {
    @NotNull
    private List<Order> orders;

    public Customer() {
    }

    public Customer(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
