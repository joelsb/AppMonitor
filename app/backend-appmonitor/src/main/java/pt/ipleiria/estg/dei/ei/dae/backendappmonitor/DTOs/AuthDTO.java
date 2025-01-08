package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import jakarta.validation.constraints.NotBlank;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;

import java.io.Serializable;

public class AuthDTO implements Serializable {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public AuthDTO() {
    }

    public AuthDTO(@NotBlank String username, @NotBlank String password) {
        this.username = username;
        this.password = password;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
}