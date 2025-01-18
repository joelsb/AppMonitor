package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageTypeDTO {

    private Long id;
    private String name;
    private List<SensorTypeDTO> mandatorySensors = new ArrayList<>();

    public PackageTypeDTO() {
    }

    public PackageTypeDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PackageTypeDTO from(PackageType packageType) {
        return new PackageTypeDTO(
                packageType.getId(),
                packageType.getName()
        );
    }

    public static List<PackageTypeDTO> from(List<PackageType> packageTypes) {
        return packageTypes.stream().map(PackageTypeDTO::from).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SensorTypeDTO> getMandatorySensors() {
        return mandatorySensors.isEmpty() ? null : new ArrayList<>(mandatorySensors);
    }

    public void setMandatorySensors(List<SensorTypeDTO> mandatorySensors) {
        this.mandatorySensors = mandatorySensors;
    }
}
