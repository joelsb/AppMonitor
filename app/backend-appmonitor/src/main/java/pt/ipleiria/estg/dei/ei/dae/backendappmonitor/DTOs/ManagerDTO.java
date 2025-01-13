package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Manager;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDTO extends UserDTO{

    private String office;

    public ManagerDTO() {
    }

    public ManagerDTO(String username, String name, String email, String office) {
        super(username, name, email, null);
        this.office = office;
    }

    public static ManagerDTO from(Manager manager) {
        return new ManagerDTO(
                manager.getUsername(),
                manager.getName(),
                manager.getEmail(),
                manager.getOffice()
        );
    }

    public List<ManagerDTO> from(List<Manager> managers) {
        return managers.stream().map(ManagerDTO::from).collect(Collectors.toList());
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }



}
