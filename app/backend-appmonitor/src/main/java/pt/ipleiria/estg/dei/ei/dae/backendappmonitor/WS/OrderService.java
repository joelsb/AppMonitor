package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;


@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {
    @EJB
    private OrderBean orderBean;

    @GET
    @Path("/")
    //Receive the Token in the header
    public Response getAllOrders(@HeaderParam("Authorization") String token) {
        //Deal with the token
        return Response.ok(OrderDTO.from((Order) orderBean.findAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        var order = orderBean.find(id);
        var orderDTO = OrderDTO.from(order);
        return Response.ok(orderDTO).build();
    }

    @GET
    @Path("/available")
    public Response getAvailableOrders() {
        return Response.ok(OrderDTO.from(orderBean.findAvailableOrders())).build();
    }

    @GET
    @Path("/{id}/volumes")
    public Response getVolumes(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volumes = orderBean.findVolumes(id);
        var volumeDTO = VolumeDTO.from(volumes);
        return Response.ok(volumeDTO).build();
    }


}
