package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.SensorTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

@Path("/sensor-types")
@Consumes("application/json")
@Produces("application/json")
@Authenticated
@RolesAllowed({"Admin"})
public class SensorTypeService {
    @EJB
    private SensorTypeBean sensorTypeBean;

    @GET
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response getAllSensorTypes() {
        var sensorTypes = sensorTypeBean.findAll();
        return Response.ok(SensorTypeDTO.fromSimple(sensorTypes)).build();
    }

    @POST
    @Path("/")
    public Response createSensorType(SensorTypeDTO sensorTypeDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        var sensorType = sensorTypeBean.create(sensorTypeDTO.getId(), sensorTypeDTO.getName(), sensorTypeDTO.getUnit(), sensorTypeDTO.getCeiling(), sensorTypeDTO.getFloor());
        return Response.ok(SensorTypeDTO.from(sensorType)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSensorType(@PathParam("id") long id, SensorTypeDTO sensorTypeDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var sensorType = sensorTypeBean.update(id, sensorTypeDTO.getName(), sensorTypeDTO.getUnit(), sensorTypeDTO.getCeiling(), sensorTypeDTO.getFloor());
        return Response.ok(SensorTypeDTO.from(sensorType)).build();
    }

    @GET
    @Path("/{id}")
    public Response getSensorType(@PathParam("id") long id) throws MyEntityNotFoundException, MyIllegalArgumentException {
        return Response.ok(SensorTypeDTO.from(sensorTypeBean.find(id))).build();
    }
}
