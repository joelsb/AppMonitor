package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.*;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.SensorTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Path("/sensor-types")
@Consumes("application/json")
@Produces("application/json")
public class SensorTypeService {
    @EJB
    private SensorTypeBean sensorTypeBean;

    @GET
    @Path("/")
    public Response getAllSensorTypes() {
        return Response.ok(SensorTypeDTO.from(sensorTypeBean.findAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getSensorType(@PathParam("id") long id) throws MyEntityNotFoundException {
        return Response.ok(SensorTypeDTO.from(sensorTypeBean.find(id))).build();
    }
}
