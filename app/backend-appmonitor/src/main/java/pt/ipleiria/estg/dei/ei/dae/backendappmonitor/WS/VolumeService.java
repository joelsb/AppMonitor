package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("volumes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Admin"})
public class VolumeService {
    @EJB
    private VolumeBean volumeBean;

    private static final Logger logger = Logger.getLogger("WS.VolumeService");

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @RolesAllowed({"Customer","Manager"})
    public Response getAllVolumes() {
        if (securityContext.isUserInRole("Customer")) {
            var volumes = volumeBean.findAllCustomerVolumes(securityContext.getUserPrincipal().getName());
            var volumesDTO = VolumeDTO.fromCustomer(volumes);

            return Response.ok(volumesDTO).build();
        }
        if(securityContext.isUserInRole("Manager") || securityContext.isUserInRole("Admin")){
            var volumes = volumeBean.findAllWithSensorsProducts();
            var volumeDTOs = VolumeDTO.fromEmployee(volumes);
            return Response.ok(VolumeDTO.fromEmployee(volumes)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
    @GET
    @Path("/available")
    @RolesAllowed({"Employee"})
    public Response getAvailableVolumes() {
        var volumes = volumeBean.findAvailableVolumes();
        var volumesDTO = VolumeDTO.from(volumes);
        return Response.ok(volumesDTO).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Customer", "Manager"})
    public Response getVolume(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volume = volumeBean.findWithSensorsProducts(id);
        if (securityContext.isUserInRole("Customer") &&
                !volume.getOrder().getCustomer().getUsername().equals(securityContext.getUserPrincipal().getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var volumeDTO = VolumeDTO.from(volume);
        volumeDTO.setProducts(ProductRecordDTO.fromSimple(volume.getProducts()));
        volumeDTO.setSensors(SensorDTO.fromSimple(volume.getSensors()));
        return Response.ok(volumeDTO).build();
    }

    @GET
    @Path("/{id}/sensors")
    @RolesAllowed({"Customer", "Manager"})
    public Response getAllSensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volume = volumeBean.findWithSensors(id);
        if (securityContext.isUserInRole("Customer") &&
                !volume.getOrder().getCustomer().getUsername().equals(securityContext.getUserPrincipal().getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var sensorsDTO = SensorDTO.fromSimple(volume.getSensors());
        //set the history to the same as the volume.getSensors().getIndex().getHistory()
        for (int i = 0; i < volume.getSensors().size(); i++) {
            sensorsDTO.get(i).setHistory(SensorRecordDTO.fromSimple(volume.getSensors().get(i).getHistory()));
        }
        //build an Answer with the DTO and a property containing the volumeId
        //Respond with the answer
        GenericDTO<Long, List<SensorDTO>> answer = new GenericDTO<>("volumeId",volume.getId(),"sensors",sensorsDTO);
        return Response.ok(answer).build();

    }

    @GET
    @Path("/{id}/products")
    @RolesAllowed({"Customer", "Manager"})
    public Response getAllProducts(@PathParam("id") long id) throws MyEntityNotFoundException {
        var volume = volumeBean.findWithProducts(id);
        if (securityContext.isUserInRole("Customer") &&
                !volume.getOrder().getCustomer().getUsername().equals(securityContext.getUserPrincipal().getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var productsDTO = ProductRecordDTO.fromSimple(volume.getProducts());
        GenericDTO<Long, List<ProductRecordDTO>> answer = new GenericDTO<>("volumeId",volume.getId(),"products",productsDTO);
        return Response.ok(answer).build();
    }

    @PATCH
    @Path("{id}/deliver")
    @RolesAllowed({"Employee"})
    public Response setVolumeDelivered(@PathParam("id") long id) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var volume = volumeBean.deliver(id);
        var volumeDTO = VolumeDTO.fromSimple(volume);
        return Response.ok(volumeDTO).build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response addVolume(VolumeCreateDTO volumeCreatedDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        volumeBean.addVolumeToOrder(volumeCreatedDTO);
        var volume = volumeBean.findWithSensorsProducts(volumeCreatedDTO.getId());
        var volumeDTO = VolumeCreateDTO.from(volume);
        volumeDTO.setProducts(ProductRecordDTO.fromSimple(volume.getProducts()));
        volumeDTO.setSensors(SensorDTO.fromSimple(volume.getSensors()));
        return Response.ok(volumeDTO).build();
    }
}
