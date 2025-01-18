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
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Authenticated;

import java.util.stream.Collectors;

@Path("/product-types")
@Consumes("application/json")
@Produces("application/json")
@Authenticated
@RolesAllowed({"Admin"})
public class ProductTypeService {

    @EJB
    private ProductTypeBean productTypeBean;

    @GET
    @Path("/")
    @RolesAllowed({"Employee"})
    public Response getAllProductTypes() {
        var productTypes = productTypeBean.findAllWithMandatorySensors();
        var productTypesDTO = ProductTypeDTO.from(productTypes);
        return Response.ok(productTypesDTO).build();
    }

    @GET
    @Path("/{id}/mandatory-sensors")
    @RolesAllowed({"Employee"})
    public Response getMandatorySensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var productType = productTypeBean.findWithMandatorySensors(id);
        var mandatorySensors = productType.getMandatorySensors();
        return Response.ok(SensorTypeDTO.fromSimple(mandatorySensors)).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductType(@PathParam("id") long id) throws MyEntityNotFoundException {
        var productType = productTypeBean.findWithMandatorySensors(id);
        var productTypeDTO = ProductTypeDTO.from(productType);
        //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
        var mandatorySensors = productType.getMandatorySensors();
        if(!mandatorySensors.isEmpty()) {
            productTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
        }
        return Response.ok(productTypeDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProductType(@PathParam("id") long id, ProductTypeDTO productTypeDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        var productType = productTypeBean.update(id, productTypeDTO.getName(),productTypeDTO.isMandatoryPackage());
        return Response.ok(ProductTypeDTO.from(productType)).build();
    }

    @POST
    @Path("/")
    public Response createProductType(ProductTypeDTO productTypeDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyIllegalArgumentException {
        System.out.println("IsMadatoryPackage: "+productTypeDTO.isMandatoryPackage());
        productTypeBean.create(productTypeDTO.getId(), productTypeDTO.getName(), productTypeDTO.isMandatoryPackage());
        ProductType productType = productTypeBean.find(productTypeDTO.getId());
        return Response.ok(ProductTypeDTO.from(productType)).build();
    }

    @POST
    @Path("/{id}/add-mandatory-sensor/{sensorTypeId}")
    public Response addMandatorySensors(@PathParam("id") long id, @PathParam("sensorTypeId") long sensorTypeId) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        productTypeBean.addMandatorySensor(id, sensorTypeId);
        var productType = productTypeBean.findWithMandatorySensors(id);
        var productTypeDTO = ProductTypeDTO.from(productType);
        productTypeDTO.setMandatorySensors(SensorTypeDTO.fromSimple(productType.getMandatorySensors()));
        return Response.ok(productTypeDTO).build();
    }
}
