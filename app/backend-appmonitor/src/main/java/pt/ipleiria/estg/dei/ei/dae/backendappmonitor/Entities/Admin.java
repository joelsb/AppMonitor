package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

import java.io.Serializable;

@NamedQueries({
        //JPQL query to get all users
        @NamedQuery(name = "getAllAdmins", query = "SELECT a FROM Admin a ORDER BY a.username"),
        @NamedQuery(name = "getAdminByEmail", query = "SELECT a FROM Admin a WHERE a.email = :email")
})

@Entity
public class Admin extends User implements Serializable {

    public Admin(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    public Admin() {
    }
}
