package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.PackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.PackageTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.stream.Collectors;

@Path("/package-types")
@Consumes("application/json")
@Produces("application/json")
public class PackageTypeService {
    @EJB
    private PackageTypeBean packageTypeBean;

    @GET
    @Path("/")
    public Response getAllPackageTypes() {
        //For every package type, define the apropiate DTO with the MandatorySensorsDTO as well
        var packageTypesDTO = packageTypeBean.findAllWithMandatorySensors().stream().map(
                packageType -> {
                    var packageTypeDTO = PackageTypeDTO.from(packageType);
                    //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
                    var mandatorySensors = packageType.getMandatorySensors();
                    //Lazy loading
                    packageTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
                    return packageTypeDTO;
                }
        ).collect(Collectors.toList());
        return Response.ok(packageTypesDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response getPackageType(@PathParam("id") long id) throws MyEntityNotFoundException {
        var packageType = packageTypeBean.findWithMandatorySensors(id);
        var packageTypeDTO = PackageTypeDTO.from(packageType);
        //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
        var mandatorySensors = packageType.getMandatorySensors();
        packageTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
        return Response.ok(packageTypeDTO).build();
    }

    @GET
    @Path("/{id}/mandatory-sensors")
    public Response getMandatorySensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var packageType = packageTypeBean.findWithMandatorySensors(id);
        var mandatorySensors = packageType.getMandatorySensors();
        return Response.ok(SensorTypeDTO.from(mandatorySensors)).build();
    }

}
