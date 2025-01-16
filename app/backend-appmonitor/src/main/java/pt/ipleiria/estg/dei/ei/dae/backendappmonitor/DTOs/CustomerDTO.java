package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.json.bind.annotation.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO extends UserDTO{

    private List<OrderDTO> orders = new ArrayList<>();
    private List<Long> ordersIds = new ArrayList<>();;

    public CustomerDTO() {
    }

    public CustomerDTO(String username,String password, String name, String email) {
        super(username, password,name, email, null);
    }

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                null,
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

    public List<OrderDTO> getOrders() {

        return orders.isEmpty() ? null : new ArrayList<>(orders);
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<Long> getOrdersIds() {

        return ordersIds.isEmpty() ? null : ordersIds;
    }

    public void setOrdersIds(List<Long> ordersIds) {
        this.ordersIds = ordersIds;
    }

}
