package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class OrderService {
    @EJB
    private OrderBean orderBean;

    private static final Logger logger = Logger.getLogger("WS.OrderService");

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @RolesAllowed({"Customer", "Manager"})
    //Customers can only see their orders
    //Managers can see all orders
    public Response getAllOrders(/*@HeaderParam("Authorization") String token*/) {
        if (securityContext.isUserInRole("Customer")) {
            var orders = orderBean.findAllCustomerOrders(securityContext.getUserPrincipal().getName());
            var ordersDTO = OrderDTO.fromCustomer(orders);
            for (Order order : orders) {
                for (OrderDTO orderDTO : ordersDTO) {
                    orderDTO.setVolumes(VolumeDTO.fromSimple(order.getVolumes()));
                }
            }
            return Response.ok(ordersDTO).build();
        }
        if (securityContext.isUserInRole("Manager")) {
            var orders = orderBean.findAll();
            var ordersDTO = OrderDTO.from(orders);

            for (Order order : orders) {
                for (OrderDTO orderDTO : ordersDTO) {
                    orderDTO.setVolumes(VolumeDTO.fromSimple(order.getVolumes()));
                }
            }
            return Response.ok(ordersDTO).build();

        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Customer", "Manager"})
    public Response getCustomerOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        //For client
        var order = orderBean.findWithVolumes(id);
        var orderDTO = OrderDTO.from(order);

        if (securityContext.isUserInRole("Customer")) {
            var principal = securityContext.getUserPrincipal();
            if (!principal.getName().equals(order.getCustomer().getUsername())) {
                // write to the log the principal.getName() and the username
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            orderDTO.setCustomerUsername(null);
        }

        var volumesDTO = VolumeDTO.from(order.getVolumes());
        for(VolumeDTO volumeDTO : volumesDTO) {
            volumeDTO.setOrderId(null);
            volumeDTO.setPackageTypeId(null);
            volumeDTO.setPackageTypeName(null);
        }
        orderDTO.setVolumes(volumesDTO);
        return Response.ok(orderDTO).build();
    }

    @GET
    @Path("/available")
    @RolesAllowed({"Employee"})
    public Response getAvailableOrders() {
        var orders = orderBean.findAvailableOrdersWithVolumes();
        var ordersDTO = OrderDTO.from(orders);
        for (OrderDTO orderDTO : ordersDTO) {
            orderDTO.setCustomerUsername(null);
        }
        return Response.ok(ordersDTO).build();
    }

    @GET
    @Path("/{id}/volumes")
    @RolesAllowed({"Customer", "Manager"})
    public Response getVolumes(@PathParam("id") long id) throws MyEntityNotFoundException {
        //For customer
        var order = orderBean.findWithVolumes(id);

        if (securityContext.isUserInRole("Customer") &&
                !order.getCustomer().getUsername().equals(securityContext.getUserPrincipal().getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();

        }
        var volumesDTO = VolumeDTO.from(order.getVolumes());
        for (VolumeDTO volumeDTO : volumesDTO) {
            volumeDTO.setPackageTypeId(null);
            volumeDTO.setOrderId(null);
        }
        GenericDTO<List<VolumeDTO>> answer = new GenericDTO<>("orderId", id, "volumes", volumesDTO);
        return Response.ok(answer).build();
    }

    @PATCH
    @Path("{id}/deliver")
    @RolesAllowed({"Employee"})
    public Response setVolumeDelivered(@PathParam("id") long id) throws MyEntityNotFoundException, MyEntityExistsException {
        var order = orderBean.deliver(id);
        var orderDTO = OrderDTO.from(order);
        orderDTO.setCustomerUsername(null);
        return Response.ok(orderDTO).build();
    }


    @POST
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response createOrder(OrderCreateDTO orderCreateDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        orderBean.create(orderCreateDTO);
        Order order = orderBean.findWithVolumesProductsSensors(orderCreateDTO.getId());
        var orderDTO = OrderDTO.from(order);
        var volumesDTO = VolumeDTO.from(order.getVolumes());
        for(VolumeDTO volumeDTO : volumesDTO) {
            volumeDTO.setOrderId(null);
            volumeDTO.setPackageTypeId(null);
        }
        //Also send the products and sensors inside the volumes
        var productsDTO = order.getVolumes().stream().map(volume ->
                ProductRecordDTO.fromSimple(volume.getProducts())).collect(Collectors.toList());
        var sensorsDTO = order.getVolumes().stream().map(volume ->
                SensorDTO.fromSimple(volume.getSensors())).collect(Collectors.toList());
        volumesDTO.forEach(volumeDTO -> {
            volumeDTO.setProducts(productsDTO.get(volumesDTO.indexOf(volumeDTO)));
            volumeDTO.setSensors(sensorsDTO.get(volumesDTO.indexOf(volumeDTO)));
        });
        orderDTO.setVolumes(volumesDTO);
        return Response.ok(orderDTO).build();

    }
}
