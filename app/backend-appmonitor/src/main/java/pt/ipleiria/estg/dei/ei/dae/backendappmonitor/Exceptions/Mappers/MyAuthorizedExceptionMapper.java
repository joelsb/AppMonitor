package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.Mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyAuthorizedException;

import java.util.logging.Logger;

@Provider
public class MyAuthorizedExceptionMapper implements ExceptionMapper<MyAuthorizedException> {
    private static final Logger logger =
            Logger.getLogger(MyAuthorizedException.class.getCanonicalName());

    @Override
    public Response toResponse(MyAuthorizedException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR MyAuthorizedException: " + errorMsg);
        return Response.status(Response.Status.CONFLICT)
                .entity(errorMsg)
                .build();
    }
}