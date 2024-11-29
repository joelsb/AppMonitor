package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDTO extends UserDTO{

    private List<OrderDTO> orders;

    public CustomerDTO() {
    }

    public CustomerDTO(String username, String password, String name, String email, List<OrderDTO> orders) {
        super(username, password, name, email);
        this.orders = orders;
    }

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getPassword(),
                customer.getName(),
                customer.getEmail(),
                OrderDTO.from(customer.getOrders())
        );
    }

    public static List<CustomerDTO> from(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::from).collect(Collectors.toList());
    }

    public List<OrderDTO> getOrders() {
        return new ArrayList<>(orders);
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }


}
