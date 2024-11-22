package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.Mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyPersistenceException;

import java.sql.SQLException;
import java.util.logging.Logger;

@Provider
public class MyPersistenceExceptionMapper implements ExceptionMapper<MyPersistenceException> {

    private static final Logger logger = Logger.getLogger(MyPersistenceExceptionMapper.class.getCanonicalName());


    @Override
    public Response toResponse(MyPersistenceException e) {
        String errorMsg = e.getMessage();

        // Check if the cause is a SQLException (e.g., database-related errors)
        Throwable cause = e.getCause();
        if (cause != null) {
            logger.warning("CAUSE of exception: " + cause.getClass().getName());

            // Recursively unwrap the cause until we find the underlying SQLException
            SQLException sqlException = getSQLException(cause);
            if (sqlException != null) {
                //MEssage: "ERROR: duplicate key value violates unique constraint "users_pkey" Detail: Key (username)=(2191618) already exists."
                //I want the DETAIL part only
                errorMsg = sqlException.getMessage().split("Detail:")[1].trim();
                // If SQLState is 23505, it indicates a unique constraint violation (e.g., duplicate username)
//                if ("23505".equals(sqlException.getSQLState())) {
//                    errorMsg = "Username already taken, please choose a different one.";
//                }
            }
        }

        // Return a generic response with the error message
        return Response.status(Response.Status.BAD_REQUEST)  // Use 400 Bad Request for duplicate
                .entity(errorMsg)
                .build();
    }

    // Helper method to recursively unwrap the cause until we find an SQLException
    private SQLException getSQLException(Throwable cause) {
        if (cause instanceof SQLException) {
            return (SQLException) cause;
        } else if (cause != null) {
            return getSQLException(cause.getCause());  // Recursively check the next cause
        }
        return null;
    }
}
