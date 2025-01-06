package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.Date;

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
    public Response getVolume(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volume = volumeBean.findWithSensorsProducts(id);
        var volumeDTO = VolumeDTO.from(volume);
        volumeDTO.setProducts(ProductRecordDTO.from(volume.getProducts()));
        volumeDTO.setSensors(SensorDTO.from(volume.getSensors()));
        return Response.ok(volumeDTO).build();
    }

    @GET
    @Path("/{id}/sensors")
    public Response getAllSensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volume = volumeBean.findWithSensorsProducts(id);
        return Response.ok(SensorDTO.from(volume.getSensors())).build();
    }
    @GET
    @Path("/{id}/products")
    public Response getAllProducts(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volume = volumeBean.findWithSensorsProducts(id);
        return Response.ok(ProductRecordDTO.from(volume.getProducts())).build();
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

    @PATCH
    @Path("{id}/delivered")
    public Response setVolumeDelivered(@PathParam("id") long id) throws MyEntityNotFoundException {
        volumeBean.setDelivered(id);
        var volume = volumeBean.find(id);
        var volumeDTO = VolumeDTO.fromManager(volume);
        return Response.ok(volumeDTO).build();
    }


}
