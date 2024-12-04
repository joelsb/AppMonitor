package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.WS;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs.ProductTypeBean;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

@Path("/product-types")
@Consumes("application/json")
@Produces("application/json")
public class ProductTypes {

    @EJB
    private ProductTypeBean productTypeBean;

    @GET
    @Path("/")
    public Response getAllProductTypes() {
        var productTypes = productTypeBean.findAllWithMandatorySensors();
        var productTypesDTO = ProductTypeDTO.from(productTypes);
        //for each productTypeDTO in productTypesDTO, set excludeMandatorySensors to true
        productTypesDTO.forEach(productTypeDTO -> productTypeDTO.setExcludeMandatorySensors(true));
        return Response.ok(productTypesDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductType(@PathParam("id") long id) throws MyEntityNotFoundException {
        var productType = productTypeBean.findWithMandatorySensors(id);
        var productTypeDTO = ProductTypeDTO.from(productType);
        return Response.ok(productTypeDTO).build();
    }
}
