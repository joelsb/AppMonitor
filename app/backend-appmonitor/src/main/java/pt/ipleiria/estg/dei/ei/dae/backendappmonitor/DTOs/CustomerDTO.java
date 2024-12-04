package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import jakarta.json.bind.annotation.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDTO extends UserDTO{

    private List<OrderDTO> orders;
    private List<Long> ordersIds;
    private boolean excludeOrders; // Flag to control serialization


    public CustomerDTO() {
    }

    public CustomerDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.orders = null;
        this.ordersIds = new ArrayList<>();
    }

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getPassword(),
                customer.getName(),
                customer.getEmail()
        );
    }

    public static CustomerDTO fromEmployee(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                null,
                customer.getName(),
                null
        );
    }

    public static List<CustomerDTO> fromAllEmployees(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::fromEmployee).collect(Collectors.toList());
    }



    public static List<CustomerDTO> from(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::from).collect(Collectors.toList());
    }

    @JsonbTransient // Prevent serialization of the raw 'orders' field
    public List<OrderDTO> getOrders() {
        return excludeOrders ? null : new ArrayList<>(orders);
    }

    public void setExcludeOrders(boolean excludeOrders) {
        this.excludeOrders = excludeOrders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<Long> getOrdersIds() {
        return ordersIds;
    }

    public void setOrdersIds(List<Long> ordersIds) {
        this.ordersIds = ordersIds;
    }

}
