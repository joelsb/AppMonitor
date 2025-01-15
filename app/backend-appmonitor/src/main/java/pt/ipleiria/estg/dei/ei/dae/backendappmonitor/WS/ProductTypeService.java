package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.ProductTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.stream.Collectors;

@Path("/product-types")
@Consumes("application/json")
@Produces("application/json")
@Authenticated
public class ProductTypeService {

    @EJB
    private ProductTypeBean productTypeBean;

    @POST
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response createProductType(ProductTypeCreateDTO productTypeCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        productTypeBean.create(productTypeCreateDTO);
        ProductType productType = productTypeBean.findByName(productTypeCreateDTO.getName());
        return Response.ok(ProductTypeDTO.from(productType)).build();
    }

    @GET
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response getAllProductTypes() {
        var productTypes = productTypeBean.findAllWithMandatorySensors();
        var productTypesDTO = ProductTypeDTO.from(productTypes);
        return Response.ok(productTypesDTO).build();
    }

//    @GET
//    @Path("/{id}")
//    public Response getProductType(@PathParam("id") long id) throws MyEntityNotFoundException {
//        var productType = productTypeBean.findWithMandatorySensors(id);
//        var productTypeDTO = ProductTypeDTO.from(productType);
//        //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
//        var mandatorySensors = productType.getMandatorySensors();
//        // Check if mandatorySensors is populated
//        System.out.println("Mandatory Sensors: " + mandatorySensors.size());
//
//        //Lazy loading
//        productTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
//        return Response.ok(productTypeDTO).build();
//    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Employee"})
    public Response updateProductType(@PathParam("id") long id, ProductTypeCreateDTO productTypeCreateDTO) throws MyEntityNotFoundException , MyEntityExistsException {
        var productType = productTypeBean.update(id, productTypeCreateDTO.getName(),productTypeCreateDTO.isMandatoryPackage());
        return Response.ok(ProductTypeDTO.from(productType)).build();
    }

    @GET
    @Path("/{id}/mandatory-sensors")
    @RolesAllowed({"Employee"})
    public Response getMandatorySensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var productType = productTypeBean.findWithMandatorySensors(id);
        var mandatorySensors = productType.getMandatorySensors();
        return Response.ok(SensorTypeDTO.fromSimple(mandatorySensors)).build();
    }

    @POST
    @Path("/{id}/add-mandatory-sensors")
    @RolesAllowed({"Employee"})
    public Response addMandatorySensors(@PathParam("id") long id, SensorTypeMandatoryDTO sensorTypeMandatoryDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        productTypeBean.addMandatorySensor(id, sensorTypeMandatoryDTO.getSensorId());
        var productType = productTypeBean.findWithMandatorySensors(id);
        var productTypeDTO = ProductTypeDTO.from(productType);
        productTypeDTO.setMandatorySensors(SensorTypeDTO.fromSimple(productType.getMandatorySensors()));
        return Response.ok(productTypeDTO).build();
    }
}
