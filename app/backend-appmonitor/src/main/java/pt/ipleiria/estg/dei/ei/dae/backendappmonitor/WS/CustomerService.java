package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.hibernate.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Path("customers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;
    private OrderBean orderBean;

//    @GET
//    @Path("/")
//    public Response getAllCustomers() {
//        return Response.ok(CustomerDTO.fromAllEmployees(customerBean.findAll())).build();
//    }

    @GET
    @Path("{username}")
    public Response getCustomerWithOrdersIds(@PathParam("username") String username) throws MyEntityNotFoundException {
        var customerWithOrders = customerBean.findWithOrders(username);
        var customerDTO = CustomerDTO.fromEmployee(customerWithOrders);

        // Retrieve orders IDs
        var ordersIds = customerWithOrders.getOrdersIds();

        // Set orders and orders IDs properly
        customerDTO.setOrdersIds(ordersIds.isEmpty() ? null : ordersIds);
        customerDTO.setExcludeOrders(true);

        return Response.ok(customerDTO).build();
    }

    @GET
    @Path("/")
    public Response getAllCustomersWithOrdersIds() throws MyEntityNotFoundException {
        var customersDTO = customerBean.findAllWithOrders().stream().map(customer -> {
            var customerDTO = CustomerDTO.fromEmployee(customer);
            var ordersIds = customer.getOrdersIds();
            customerDTO.setOrdersIds(ordersIds.isEmpty() ? null : ordersIds);
            customerDTO.setExcludeOrders(true);
            return customerDTO;
        }).collect(Collectors.toList());
        return Response.ok(customersDTO).build();
    }

    @GET
    @Path("/orders/{id}/volumes")
    public Response getCustomerOrderVolumes(@PathParam("id") long id) throws MyEntityNotFoundException {
        var order = orderBean.find(id);
        var orderDTO = OrderDTO.from(order);
        var volumes = order.getVolumes();
        orderDTO.setVolumes(volumes.isEmpty() ? null : VolumeDTO.from(volumes));
        return Response.ok(orderDTO).build();
    }




}
