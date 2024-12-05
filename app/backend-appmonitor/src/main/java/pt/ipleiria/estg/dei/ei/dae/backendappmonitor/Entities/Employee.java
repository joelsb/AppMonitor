package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;

@NamedQueries(
        {
                //JPQL query to get all users
                @NamedQuery(name = "getAllEmployees", query = "SELECT e FROM Employee e ORDER BY e.username"),
                @NamedQuery(name = "getEmployeeByEmail", query = "SELECT e FROM Employee e WHERE e.email = :email")
        }
)

@Entity
public class Employee extends User implements Serializable {
    @NotNull
    private String warehouse;

    public Employee() {
    }

    public Employee(String username, String password, String name, String email, String warehouse) {
        super(username, password, name, email);
        this.warehouse = warehouse;
    }

    public @NotNull String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(@NotNull String warehouse) {
        this.warehouse = warehouse;
    }

}
