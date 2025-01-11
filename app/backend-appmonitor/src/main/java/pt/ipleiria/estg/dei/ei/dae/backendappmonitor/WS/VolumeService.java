package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.Date;
import java.util.logging.Logger;

@Path("volumes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class VolumeService {
    @EJB
    private VolumeBean volumeBean;

    private static final Logger logger = Logger.getLogger("WS.VolumeService");

    @GET
    @Path("/")
    @RolesAllowed({"Customer"})
    public Response getAllVolumes() {
        var volumes = volumeBean.findAll();
        logger.info("Volumes: " + volumes.get(0).getSentDate());
        var volumeDTOs = VolumeDTO.fromEmployee(volumes);
        logger.info("VolumeDTOs: " + volumeDTOs.get(0).getSentDate());
        return Response.ok(VolumeDTO.fromEmployee(volumes)).build();
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
    @Path("{id}/deliver")
    public Response setVolumeDelivered(@PathParam("id") long id) throws MyEntityNotFoundException, MyIllegalArgumentException {
        volumeBean.deliver(id);
        var volume = volumeBean.find(id);
        var volumeDTO = VolumeDTO.fromManager(volume);
        return Response.ok(volumeDTO).build();
    }


}
