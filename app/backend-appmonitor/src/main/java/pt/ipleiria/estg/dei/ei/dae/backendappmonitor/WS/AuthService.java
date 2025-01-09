package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;


import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.TokenIssuer;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class AuthService {

    @Inject
    private TokenIssuer issuer;
    @EJB
    private UserBean userBean;
    @Context
    private SecurityContext security;

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {
            String token = issuer.issue(auth.getUsername());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = security.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UserDTO.fromUser(user)).build();
    }

}