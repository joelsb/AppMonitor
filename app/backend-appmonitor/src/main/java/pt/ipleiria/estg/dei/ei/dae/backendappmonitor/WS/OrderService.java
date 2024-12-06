package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
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

    @POST
    @Path("/")
    public Response createOrder(OrderCreateDTO orderCreateDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        orderBean.create(orderCreateDTO);
        Order order = orderBean.findWithVolumes(orderCreateDTO.getId());
        var orderDTO = OrderDTO.from(order);
        var volumesDTO = VolumeDTO.from(order.getVolumes());
        orderDTO.setVolumes(volumesDTO);
        return Response.ok(orderDTO).build();
    }

}
