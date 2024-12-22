package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Path("users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserService {
    @EJB
    private UserBean userBean;

    @GET
    @Path("/")
    public Response getAllUsers() {
        return Response.ok(UserDTO.fromUsers(userBean.findAll())).build();
    }

    @GET
    @Path("{username}")
    public Response getUser(@PathParam("username") String username) throws MyEntityNotFoundException {
        return Response.ok(UserDTO.fromUser(userBean.find(username))).build();
    }

}
