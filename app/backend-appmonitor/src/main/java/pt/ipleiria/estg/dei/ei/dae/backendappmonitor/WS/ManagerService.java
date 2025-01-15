package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ManagerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.ManagerBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;


import jakarta.annotation.security.*;
import jakarta.ws.rs.core.*;

@Path("managers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class ManagerService {

    @EJB
    private ManagerBean managerBean;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/")
    @RolesAllowed({"Manager"})
    public Response createManager(ManagerDTO managerDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        managerBean.create(managerDTO.getUsername(), managerDTO.getPassword(), managerDTO.getName(), managerDTO.getEmail(),managerDTO.getOffice());
        var manager = managerBean.find(managerDTO.getUsername());
        var managerDTO1 = ManagerDTO.from(manager);
        return Response.ok(managerDTO1).build();
    }

    @PUT
    @Path("/{username}")
    @RolesAllowed({"Manager"})
    public Response updateManager(@PathParam("username") String username, ManagerDTO managerDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        var manager = managerBean.find(username);
        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        managerBean.update(username, managerDTO.getName(), managerDTO.getEmail(), managerDTO.getOffice());
        manager = managerBean.find(username);
        managerDTO = ManagerDTO.from(manager);
        return Response.ok(managerDTO).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"Manager"})
    public Response getManager(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var manager = managerBean.find(username);
        var managerDTO = ManagerDTO.from(manager);
        return Response.ok(managerDTO).build();
    }

}
