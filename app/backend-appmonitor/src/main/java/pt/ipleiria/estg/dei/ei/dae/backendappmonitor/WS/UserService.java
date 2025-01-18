package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

@Path("users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Admin"})
public class UserService {
    @EJB
    private UserBean userBean;
    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAllUsers() {
        return Response.ok(UserDTO.fromUsers(userBean.findAll())).build();
    }

    //METHOD IN AUTH SERVICE ALREADY
//    @GET
//    @Path("{username}")
//    @RolesAllowed({"Customer", "Manager", "Employee"})
//    public Response getUser(@PathParam("username") String username) throws MyEntityNotFoundException {
//        if(securityContext.isUserInRole("Customer") && !securityContext.getUserPrincipal().getName().equals(username)){
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//        if (securityContext.isUserInRole("Manager") && !securityContext.getUserPrincipal().getName().equals(username)) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//        if(securityContext.isUserInRole("Employee") && !securityContext.getUserPrincipal().getName().equals(username)){
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//        return Response.ok(UserDTO.fromUser(userBean.find(username))).build();
//    }

}
