package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/{id}")
    public Response getSensorData(@PathParam("id") long id) throws MyEntityNotFoundException {
        var history = sensorBean.find(id).getHistory();
        var sensorDTO = SensorDTO.from(sensorBean.find(id));
        return Response.ok().build();
    }
}