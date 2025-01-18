package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

@Path("emails")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class EmailService {

    @EJB
    private EmailBean emailBean;
    @EJB
    private UserBean userBean;

    //Only for testing purposes
    @POST
    @Path("/{username}")
    public Response sendEmail(@PathParam("username") String username, EmailDTO emailDTO) throws MyEntityNotFoundException {
        var user = userBean.findOrFail(username);
        emailBean.send(user.getEmail(), emailDTO.getSubject(), emailDTO.getBody());
        return Response.status(Response.Status.OK).entity("Email Sent to: '"+user.getEmail()+"'").build();
    }
}
