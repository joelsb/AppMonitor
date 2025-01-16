package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.*;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import org.hibernate.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.EmployeeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.*;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("employees")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class EmployeeService {
    @EJB
    private EmployeeBean employeeBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("{username}")
    @RolesAllowed({"Employee"})
    public Response getEmployee(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if(!principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        var employee = employeeBean.find(username);
        var employeeDTO = EmployeeDTO.from(employee);
        return Response.ok(employeeDTO).build();
    }

    @PUT
    @Path("{username}")
    @RolesAllowed({"Employee"})
    public Response updateEmployee(@PathParam("username") String username, EmployeeDTO employeeDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username) || !principal.getName().equals(employeeDTO.getUsername())) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var employee = employeeBean.update(employeeDTO.getUsername(), employeeDTO.getName(), employeeDTO.getEmail(), employeeDTO.getWarehouse());

        return Response.ok(EmployeeDTO.from(employee)).build();
    }
}
