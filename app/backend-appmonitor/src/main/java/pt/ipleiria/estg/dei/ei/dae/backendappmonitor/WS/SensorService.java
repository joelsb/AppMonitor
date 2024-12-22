package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
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
        var sensor = sensorBean.findWithHistoryAndSensorType(id);
        var sensorDTO = SensorDTO.fromHistory(sensor);
        sensorDTO.setHistory(SensorRecordDTO.fromSensor(sensor.getHistory()));
        sensorDTO.setSensorType(SensorTypeDTO.from(sensor.getSensorType()));
        return Response.ok(sensorDTO).build();
    }
}
