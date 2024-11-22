package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.Mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;

import java.util.logging.Logger;

@Provider
public class MyIllegalArgumentExceptionMapper implements ExceptionMapper<MyIllegalArgumentException> {

    private static final Logger logger = Logger.getLogger(MyIllegalArgumentException.class.getCanonicalName());

    @Override
    public Response toResponse(MyIllegalArgumentException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMsg)
                .build();
    }
}
