package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.AdminDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.AdminBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Admin;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

@Path("admins")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Admin"})
public class AdminService {
    @EJB
    private AdminBean adminBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getAdmins() {
        var admins = adminBean.findAll();
        return Response.ok(AdminDTO.from(admins)).build();
    }

    @POST
    @Path("/")
    public Response createAdmin(AdminDTO adminDTO) throws MyEntityExistsException, MyIllegalArgumentException {
        var admin = adminBean.create(adminDTO.getUsername(), adminDTO.getPassword(), adminDTO.getName(), adminDTO.getEmail());
        return Response.ok(AdminDTO.from(admin)).build();
    }

    @GET
    @Path("/{username}")
    public Response getAdmin(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Admin admin = adminBean.find(username);

        return Response.ok(AdminDTO.from(admin)).build();
    }

    @PUT
    @Path("/{username}")
    public Response updateAdmin(@PathParam("username") String username, AdminDTO adminDTO) throws MyEntityExistsException, MyIllegalArgumentException {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Admin admin = adminBean.update(username, adminDTO.getName(), adminDTO.getEmail());
        return Response.ok(AdminDTO.from(admin)).build();
    }
}
