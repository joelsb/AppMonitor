package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.validation.constraints.NotNull;

public class Manager extends User{
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
