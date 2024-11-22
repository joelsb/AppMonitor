package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions;

import java.sql.SQLException;

public class MyPersistenceException extends Exception {

    public MyPersistenceException(String message, SQLException cause) {
        super(message, cause);
    }

    public MyPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
