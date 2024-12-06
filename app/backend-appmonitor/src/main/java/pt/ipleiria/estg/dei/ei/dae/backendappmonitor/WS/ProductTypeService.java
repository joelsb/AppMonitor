package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.ProductTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.stream.Collectors;

@Path("/product-types")
@Consumes("application/json")
@Produces("application/json")
public class ProductTypeService {

    @EJB
    private ProductTypeBean productTypeBean;

    @GET
    @Path("/")
    public Response getAllProductTypes() {
        var productTypesDTO = productTypeBean.findAllWithMandatorySensors().stream().map(
                productType -> {
                    var productTypeDTO = ProductTypeDTO.from(productType);
                    //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
                    var mandatorySensors = productType.getMandatorySensors();
                    //Lazy loading
                    productTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
                    productTypeDTO.setExcludeMandatorySensors(mandatorySensors.isEmpty());
                    return productTypeDTO;
                }
        ).collect(Collectors.toList());
        return Response.ok(productTypesDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductType(@PathParam("id") long id) throws MyEntityNotFoundException {
        var productType = productTypeBean.findWithMandatorySensors(id);
        var productTypeDTO = ProductTypeDTO.from(productType);
        //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true if the productType.getMandatorySensors() is empty
        var mandatorySensors = productType.getMandatorySensors();
        // Check if mandatorySensors is populated
        System.out.println("Mandatory Sensors: " + mandatorySensors.size());

        //Lazy loading
        productTypeDTO.setMandatorySensors(SensorTypeDTO.from(mandatorySensors));
        productTypeDTO.setExcludeMandatorySensors(mandatorySensors.isEmpty());
        return Response.ok(productTypeDTO).build();
    }

    @GET
    @Path("/{id}/mandatory-sensors")
    public Response getMandatorySensors(@PathParam("id") long id) throws MyEntityNotFoundException {
        var productType = productTypeBean.findWithMandatorySensors(id);
        var mandatorySensors = productType.getMandatorySensors();
        return Response.ok(SensorTypeDTO.from(mandatorySensors)).build();
    }
}
