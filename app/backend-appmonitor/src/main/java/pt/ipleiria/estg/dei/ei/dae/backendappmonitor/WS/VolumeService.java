package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

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

    @POST
    @Path("/")
    public Response addVolume(VolumeCreateDTO volumeCreatedDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        volumeBean.addVolumeToOrder(volumeCreatedDTO);
        var volume = volumeBean.findWithSensorsProducts(volumeCreatedDTO.getId());
        var volumeDTO = VolumeCreateDTO.from(volume);
        volumeDTO.setProducts(ProductRecordDTO.from(volume.getProducts()));
        volumeDTO.setSensors(SensorDTO.from(volume.getSensors()));
        return Response.ok(volumeDTO).build();
    }


}
