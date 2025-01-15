package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.GenericDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.List;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {

    @EJB
    private SensorBean sensorBean;
    @EJB
    private EmailBean emailBean;
    @EJB
    private CustomerBean customerBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @Authenticated
    @RolesAllowed({"Employee"})
    public Response getAllSensors() {
        var sensors = sensorBean.findAllWithHistory();
        var sensorsDTO = SensorDTO.fromSimple(sensors);
        for (int i = 0; i < sensors.size(); i++) {
            sensorsDTO.get(i).setHistory(SensorRecordDTO.fromSimple(sensors.get(i).getHistory()));
        }
        //get the volumeId
        var volumeId = sensors.get(0).getVolume().getId();
        GenericDTO<List<SensorDTO>> answer = new GenericDTO<>("volumeId", volumeId, "sensors", sensorsDTO);
        return Response.ok(answer).build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    @RolesAllowed({"Manager", "Customer"})
    public Response getSensorData(@PathParam("id") long id) throws MyEntityNotFoundException {
        var sensor = sensorBean.findWithHistory(id);
        if (securityContext.isUserInRole("Customer") &&
                !sensor.getVolume().getOrder().getCustomer().getUsername().equals(securityContext.getUserPrincipal().getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var sensorDTO = SensorDTO.fromSimpleWithVolumeId(sensor);
        sensorDTO.setHistory(SensorRecordDTO.fromSimple(sensor.getHistory()));
        return Response.ok(sensorDTO).build();
    }

    @POST
    @Path("/{sensorId}/add-value")
    public Response addValueToSensor(@PathParam("sensorId") Long sensorId, SensorRecordDTO sensorRecordDTO) throws MyEntityNotFoundException {
        sensorBean.addValue(sensorId, sensorRecordDTO);
        var sensor = sensorBean.findWithHistory(sensorId);
        var sensorDTO = SensorDTO.from(sensor);
        sensorDTO.setSensorTypeId(null);
        sensorDTO.setHistory(SensorRecordDTO.fromSimple(sensor.getHistory()));
        return Response.ok(sensorDTO).build();
    }

//    @POST
//    @Path("/{username}/email")
//    public Response sendEmail(@PathParam("username") String username, EmailDTO email) throws MyEntityNotFoundException, MessagingException {
//        var customer = customerBean.find(username);
//        emailBean.send(customer.getEmail(), email.getSubject(), email.getBody());
//        return Response.status(Response.Status.OK).entity("E-mail sent").build();
//    }


}
