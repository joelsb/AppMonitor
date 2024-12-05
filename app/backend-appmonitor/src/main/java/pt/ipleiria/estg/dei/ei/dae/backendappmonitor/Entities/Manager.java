package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;

@NamedQueries(
        {
                //JPQL query to get all users
                @NamedQuery(name = "getAllManagers", query = "SELECT m FROM Manager m ORDER BY m.username"),
                @NamedQuery(name = "getManagerByEmail", query = "SELECT m FROM Manager m WHERE m.email = :email")
        }
)

@Entity
public class Manager extends User implements Serializable {
    @NotNull
    private String office;

    public Manager() {
    }

    public Manager(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
        this.office = office;
    }

    public @NotNull String getOffice() {
        return office;
    }

    public void setOffice(@NotNull String office) {
        this.office = office;
    }
}
