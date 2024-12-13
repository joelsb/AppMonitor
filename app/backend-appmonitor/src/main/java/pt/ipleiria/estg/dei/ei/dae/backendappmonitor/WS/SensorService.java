package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;

@Path("/sensors")
@Consumes("application/json")
@Produces("application/json")
public class SensorService {
//    @EJB
//    private SensorBean sensorBean;
//
    @GET
    @Path("/")
    public Response getAllSensors() {
//        SensorDTO.from(sensorBean.findAll())
        return Response.ok().build();
    }
//
//    @POST
//    @Path("/{id}/add-value")
//    public Response addValue(@PathParam("id") long id, SensorAddValueDTO sensorAddValueDTO) {
//        sensorBean.addValue(id, sensorAddValueDTO.getValue());
//        var sensor = sensorBean.findWithHistory(id);
//        return Response.ok(SensorDTO.from(sensor)).build();
//    }

}
