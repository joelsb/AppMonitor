package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.PackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.PackageTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Path("/package-types")
@Consumes("application/json")
@Produces("application/json")
public class PackageTypeService {
    @EJB
    private PackageTypeBean packageTypeBean;

    @GET
    @Path("/")
    public Response getAllPackageTypes() {
        return Response.ok(PackageTypeDTO.from(packageTypeBean.findAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getPackageType(@PathParam("id") long id) throws MyEntityNotFoundException {
        return Response.ok(PackageTypeDTO.from(packageTypeBean.find(id))).build();
    }

}
