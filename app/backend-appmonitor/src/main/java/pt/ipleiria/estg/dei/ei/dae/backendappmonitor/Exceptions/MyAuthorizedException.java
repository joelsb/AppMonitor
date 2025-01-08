package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

@Provider
public class MyAuthorizedException extends Exception {

    public MyAuthorizedException(String message) {
        super(message);
    }


}