package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.VolumeBean;

@Path("volumes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class VolumeService {
    @EJB
    private VolumeBean volumeBean;

    @GET
    @Path("/")
    public Response getAllVolumes() {
        return Response.ok(VolumeDTO.from(volumeBean.findAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getVolume(@PathParam("id") long id) {
        return Response.ok(VolumeDTO.from(volumeBean.findAll())).build();
    }


}
