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
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

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
    @RolesAllowed({"Customer","Manager"})
    //Receive the Token in the header
    public Response getAllOrders(/*@HeaderParam("Authorization") String token*/) {
        //For client
        if(securityContext.isUserInRole("Customer")){
            var orders = orderBean.findAllCustomerOrders(securityContext.getUserPrincipal().getName());
            var ordersDTO = OrderDTO.from(orders);
            for (Order order : orders) {
                for (OrderDTO orderDTO : ordersDTO) {
                    orderDTO.setVolumes(VolumeDTO.fromCustomer(order.getVolumes()));
                }
            }
            return Response.ok(ordersDTO).build();
        }
        if(securityContext.isUserInRole("Manager")){
            var orders = orderBean.findAll();
            var ordersDTO = OrderDTO.fromManager(orders);

            for (Order order : orders) {
                for (OrderDTO orderDTO : ordersDTO) {
                    orderDTO.setVolumes(VolumeDTO.fromCustomer(order.getVolumes()));
                }
            }
            return Response.ok(ordersDTO).build();

        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        //For client
        /*
        var order = orderBean.findWithVolumes(id);
        var orderDTO = OrderDTO.from(order);
        orderDTO.setVolumes(VolumeDTO.fromManager(order.getVolumes()));
        return Response.ok(orderDTO).build();*/
        //Verify role of client


        //For Manager
        var order = orderBean.findWithVolumes(id);
        var orderDTO = OrderDTO.fromManager(order);
        orderDTO.setVolumes(VolumeDTO.from(order.getVolumes()));
        return Response.ok(orderDTO).build();
    }


    @POST
    @Path("/")
    public Response createOrder(OrderCreateDTO orderCreateDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        orderBean.create(orderCreateDTO);
        Order order = orderBean.findWithVolumesProductsSensors(orderCreateDTO.getId());
        var orderDTO = OrderDTO.from(order);
        var volumesDTO = VolumeDTO.from(order.getVolumes());
        //Also send the products and sensors inside the volumes
        var productsDTO = order.getVolumes().stream().map(volume ->
                ProductRecordDTO.from(volume.getProducts())).collect(Collectors.toList());
        var sensorsDTO = order.getVolumes().stream().map(volume ->
                        SensorDTO.from(volume.getSensors())).collect(Collectors.toList());
        volumesDTO.forEach(volumeDTO -> {
            volumeDTO.setProducts(productsDTO.get(volumesDTO.indexOf(volumeDTO)));
            volumeDTO.setSensors(sensorsDTO.get(volumesDTO.indexOf(volumeDTO)));
        });
        orderDTO.setVolumes(volumesDTO);
        return Response.ok(orderDTO).build();

    }

    @GET
    @Path("/available")
    public Response getAvailableOrders() {
        var orders = orderBean.findAvailableOrders();
        var ordersDTO = OrderDTO.from(orders);
        for (Order order : orders) {
            for (OrderDTO orderDTO : ordersDTO) {
                orderDTO.setVolumes(VolumeDTO.fromEmployee(order.getVolumes()));
            }
        }
        return Response.ok(ordersDTO).build();
    }

    @GET
    @Path("/{id}/volumes")
    public Response getVolumes(@PathParam("id") long id) throws MyEntityNotFoundException {
        //For Manager
        /*
        var order = orderBean.findWithVolumes(id);
        var volumeDTO = VolumeDTO.fromManager(order.getVolumes());
        return Response.ok(volumeDTO).build();
        * */


        //For client
        var order = orderBean.findWithVolumes(id);
        var volumeDTO = VolumeDTO.from(order.getVolumes());
        return Response.ok(volumeDTO).build();
    }

    @PATCH
    @Path("{id}/deliver")
    public Response setVolumeDelivered(@PathParam("id") long id) throws MyEntityNotFoundException, MyEntityExistsException {
        orderBean.deliver(id);
        var order = orderBean.find(id);
        var orderDTO = OrderDTO.fromManager(order);
        return Response.ok(orderDTO).build();
    }


}
