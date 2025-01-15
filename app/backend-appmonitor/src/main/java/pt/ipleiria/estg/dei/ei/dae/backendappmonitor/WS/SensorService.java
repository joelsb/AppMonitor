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
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.Date;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/")
    public Response getAllSensors() {
        return Response.ok(SensorDTO.fromSensorService(sensorBean.findAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getSensorData(@PathParam("id") long id) throws MyEntityNotFoundException {
        var sensor = sensorBean.findWithHistory(id);
        var sensorDTO = SensorDTO.fromSensorService(sensor);
        sensorDTO.setHistory(SensorRecordDTO.fromSensor(sensor.getHistory()));
        return Response.ok(sensorDTO).build();
    }

    @POST
    @Path("/{sensorId}/add-value")
    public Response addValueToSensor(@PathParam("sensorId") Long sensorId, SensorRecordDTO sensorRecordDTO) throws MyEntityNotFoundException {
        sensorBean.addValue(sensorId, sensorRecordDTO);
        var sensor = sensorBean.findWithHistory(sensorId);
        var sensorDTO = SensorDTO.from(sensor);
        return Response.ok(sensorDTO).build();
    }


}
