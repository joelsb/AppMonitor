package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;

@Path("customers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;

    @GET
    @Path("/")
    public Response getAllCustomers() {
        return Response.ok(CustomerDTO.from((Customer) customerBean.findAll())).build();
    }

    @GET
    @Path("{username}")
    public Response getCustomer(@PathParam("username") String username) {
        return Response.ok(CustomerDTO.from((Customer)customerBean.find(username))).build();
    }


}
