package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.*;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.SensorTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

@Path("/sensor-types")
@Consumes("application/json")
@Produces("application/json")
@Authenticated
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
    @RolesAllowed({"Employee"})
    public Response createSensorType(SensorTypeCreateDTO sensorTypeCreateDTO) throws MyEntityNotFoundException , MyEntityExistsException {
        var sensorType = sensorTypeBean.create(sensorTypeCreateDTO);
        return Response.ok(SensorTypeDTO.from(sensorType)).build();
    }

//    @GET
//    @Path("/{id}")
//    public Response getSensorType(@PathParam("id") long id) throws MyEntityNotFoundException {
//        return Response.ok(SensorTypeDTO.from(sensorTypeBean.find(id))).build();
//    }
}
