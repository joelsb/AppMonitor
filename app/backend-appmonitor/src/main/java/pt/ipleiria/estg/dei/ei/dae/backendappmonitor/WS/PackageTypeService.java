package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.PackageTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.stream.Collectors;

@Path("/package-types")
@Consumes("application/json")
@Produces("application/json")
@Authenticated
public class PackageTypeService {
    @EJB
    private PackageTypeBean packageTypeBean;

    @GET
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response getAllPackageTypes() {
        //For every package type, define the apropiate DTO with the MandatorySensorsDTO as well
        var packageTypes = packageTypeBean.findAllWithMandatorySensors();
        var packageTypesDTO = PackageTypeDTO.from(packageTypes);
        return Response.ok(packageTypesDTO).build();
    }

//    @GET
//    @Path("/{id}")
//    public Response getPackageType(@PathParam("id") long id) throws MyEntityNotFoundException {
//        var packageType = packageTypeBean.findWithMandatorySensors(id);
//        var packageTypeDTO = PackageTypeDTO.from(packageType);
//        //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
//        var mandatorySensors = packageType.getMandatorySensors();
//        packageTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
//        return Response.ok(packageTypeDTO).build();
//    }

    @GET
    @Path("/{id}/mandatory-sensors")
    @RolesAllowed({"Employee"})
    public Response getMandatorySensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var packageType = packageTypeBean.findWithMandatorySensors(id);
        var mandatorySensors = packageType.getMandatorySensors();
        return Response.ok(SensorTypeDTO.fromSimple(mandatorySensors)).build();
    }

    @POST
    @Path("/{id}/add-mandatory-sensors")
    @RolesAllowed({"Employee"})
    public Response addMandatorySensors(@PathParam("id") long id, SensorTypeMandatoryDTO sensorTypeMandatoryDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        packageTypeBean.addMandatorySensor(id, sensorTypeMandatoryDTO.getSensorId());
        var packageType = packageTypeBean.findWithMandatorySensors(id);
        var packageTypeDTO = PackageTypeDTO.from(packageType);
        packageTypeDTO.setMandatorySensors(SensorTypeDTO.fromSimple(packageType.getMandatorySensors()));
        return Response.ok(packageTypeDTO).build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response createPackageType(PackageTypeDTO packageTypeDTO) throws MyEntityExistsException , MyEntityNotFoundException {
        packageTypeBean.create(packageTypeDTO);
        var packageType = packageTypeBean.find(packageTypeDTO.getId());
        packageTypeDTO = PackageTypeDTO.from(packageType);
        return Response.ok(packageTypeDTO).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Employee"})
    public Response updatePackageType(@PathParam("id") long id, PackageTypeCreateDTO packageTypeCreateDTO) throws MyEntityNotFoundException , MyEntityExistsException {
        var packageType = packageTypeBean.update(id, packageTypeCreateDTO.getName());
        return Response.ok(PackageTypeDTO.from(packageType)).build();
    }

}
