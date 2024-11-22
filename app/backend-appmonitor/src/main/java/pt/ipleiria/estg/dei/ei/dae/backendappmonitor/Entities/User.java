package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "users",
        //Unique constraint to avoid duplicate usernames and emails
        uniqueConstraints = { @UniqueConstraint(columnNames = "email") })

@NamedQueries({
        //JPQL query to get all users
        @NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u ORDER BY u.username") ,
        @NamedQuery(name = "getUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
// “single table per class hierarchy” strategy
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User  extends Versionable{

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    private boolean isActive;

    public User() {
        this.isActive = true;
    }

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isActive = true;
    }

    public String getUsername() {
        return username;
    }


    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email String email) {
        this.email = email;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(@NotNull boolean active) {
        isActive = active;
    }
}
