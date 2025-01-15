package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.*;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import org.hibernate.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;


import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("customers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class CustomerService {
    @EJB
    private CustomerBean customerBean;
    @EJB
    private OrderBean orderBean;

    @Context
    private SecurityContext securityContext;

    private static final Logger logger = Logger.getLogger("WS.CustomerService");


    @POST
    @Path("/")
    public Response createCustomer(CustomerDTO customerCreateDTO) throws MyEntityNotFoundException , MyEntityExistsException {
        customerBean.create(customerCreateDTO.getUsername(), customerCreateDTO.getPassword(), customerCreateDTO.getName(), customerCreateDTO.getEmail());
        Customer customer = customerBean.find(customerCreateDTO.getUsername());
        var customerDTO = CustomerDTO.from(customer);
        return Response.ok(customerDTO).build();
    }

    @PUT
    @Path("/{username}")
    @RolesAllowed({"Customer"})
    public Response updateCustomer(@PathParam("username") String username, CustomerDTO customerDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        Customer customer = customerBean.find(username);
        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        customerBean.update(username, customerDTO.getName(), customerDTO.getEmail());
        customer = customerBean.find(username);
        customerDTO = CustomerDTO.from(customer);
        return Response.ok(customerDTO).build();
    }


    //Only Customer can access this
    @GET
    @Path("{username}")
    @RolesAllowed({"Customer"})
    public Response getCustomerWithOrdersIds(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        var customerWithOrders = customerBean.findWithOrders(username);
        var customerDTO = CustomerDTO.from(customerWithOrders);

        // Set orders and orders IDs properly
        customerDTO.setOrdersIds(customerWithOrders.getOrdersIds());

        return Response.ok(customerDTO).build();
    }

    //Only Employee can access this
    @GET
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response getAllCustomers() {
        var customersDTO = customerBean.findAllWithOrders().stream().map(customer -> {
            var customerDTO = CustomerDTO.fromEmployee(customer);
            var ordersIds = customer.getOrdersIds();
            customerDTO.setOrdersIds(ordersIds);
            return customerDTO;
        }).collect(Collectors.toList());
        return Response.ok(customersDTO).build();
    }

//    @GET
//    @Path("/orders")
//    public Response getAllCustomersWithOrdersIds() throws MyEntityNotFoundException {
//        var customersDTO = customerBean.findAllWithOrders().stream().map(customer -> {
//            var customerDTO = CustomerDTO.fromEmployee(customer);
//            var ordersIds = customer.getOrdersIds();
//            customerDTO.setOrdersIds(ordersIds.isEmpty() ? null : ordersIds);
//            return customerDTO;
//        }).collect(Collectors.toList());
//        return Response.ok(customersDTO).build();
//    }
//
    @GET
    @Path("/{username}/orders")
    @RolesAllowed({"Manager"})
    public Response getAllOrdersByCustomerId(@PathParam("username") String username) throws MyEntityNotFoundException {
        var orders = orderBean.findAllCustomerOrders(username);
        var ordersDTO = OrderDTO.from(orders);
        for (int i = 0; i < orders.size(); i++) {
            ordersDTO.get(i).setVolumes(VolumeDTO.fromSimple(orders.get(i).getVolumes()));
        }
        return Response.ok(ordersDTO).build();
    }

//    @GET
//    @Path("/orders/{id}/volumes")
//    public Response getCustomerOrderVolumes(@PathParam("id") long id) throws MyEntityNotFoundException {
//        var order = orderBean.find(id);
//        var orderDTO = OrderDTO.from(order);
//        var volumes = order.getVolumes();
//        orderDTO.setVolumes(volumes.isEmpty() ? null : VolumeDTO.from(volumes));
//        return Response.ok(orderDTO).build();
//    }

}
