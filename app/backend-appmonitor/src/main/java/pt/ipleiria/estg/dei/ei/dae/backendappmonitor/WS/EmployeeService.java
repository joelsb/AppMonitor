package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.*;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.EmployeeBean;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.*;

@Path("employees")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Admin"})
public class EmployeeService {
    @EJB
    private EmployeeBean employeeBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public Response getEmployees() {
        var employees = employeeBean.findAllEmployees();
        return Response.ok(EmployeeDTO.from(employees)).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"Employee"})
    public Response getEmployee(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if(securityContext.isUserInRole("Employee") && !principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        var employee = employeeBean.find(username);
        var employeeDTO = EmployeeDTO.from(employee);
        return Response.ok(employeeDTO).build();
    }

    @PUT
    @Path("/{username}")
    @RolesAllowed({"Employee"})
    public Response updateEmployee(@PathParam("username") String username, EmployeeDTO employeeDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Employee") && !principal.getName().equals(username)) {
            // write to the log the principal.getName() and the username
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var employee = employeeBean.update(username, employeeDTO.getName(), employeeDTO.getEmail(), employeeDTO.getWarehouse());
        return Response.ok(EmployeeDTO.from(employee)).build();
    }

    @POST
    @Path("/")
    public Response createEmployee(EmployeeDTO employeeCreateDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        employeeBean.create(employeeCreateDTO.getUsername(), employeeCreateDTO.getPassword(), employeeCreateDTO.getName(), employeeCreateDTO.getEmail(), employeeCreateDTO.getWarehouse());
        Employee employee = employeeBean.find(employeeCreateDTO.getUsername());
        var employeeDTO = EmployeeDTO.from(employee);
        return Response.ok(employeeDTO).build();
    }

}
