package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class Employee extends User{
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
