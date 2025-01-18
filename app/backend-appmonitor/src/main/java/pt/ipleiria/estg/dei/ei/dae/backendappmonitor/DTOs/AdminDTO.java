package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Admin;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDTO extends UserDTO {

    public AdminDTO() {
    }

    public AdminDTO(String username, String password, String name, String email) {
        super(username, password, name, email, null);
    }

    public static AdminDTO from(Admin admin) {
        return new AdminDTO(
                admin.getUsername(),
                null,
                admin.getName(),
                admin.getEmail()
        );
    }

    public static List<AdminDTO> from(List<Admin> admins) {
        return admins.stream().map(AdminDTO::from).collect(Collectors.toList());
    }
}
