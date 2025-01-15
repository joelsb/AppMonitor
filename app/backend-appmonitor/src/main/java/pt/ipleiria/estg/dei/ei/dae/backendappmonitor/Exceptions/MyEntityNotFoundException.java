package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class MyEntityNotFoundException extends RuntimeException {

    public MyEntityNotFoundException(String message) {
        super(message);
    }
}
