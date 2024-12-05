package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PackageTypeDTO {
    /*
    id: long
    name: String
    mandatorySensors: List<SensorType>
     */

    private long id;
    private String name;
    private List<SensorTypeDTO> mandatorySensors;

    public PackageTypeDTO() {
    }

    public PackageTypeDTO(long id, String name) {
        this.id = id;
        this.name = name;
        this.mandatorySensors = new ArrayList<>();
    }

    public static PackageTypeDTO from(PackageType packageType) {
        return new PackageTypeDTO(
                packageType.getId(),
                packageType.getName()
        );
    }

    public List<PackageTypeDTO> from(List<PackageType> packageTypes) {
        return packageTypes.stream().map(PackageTypeDTO::from).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SensorTypeDTO> getMandatorySensors() {
        return mandatorySensors;
    }

    public void setMandatorySensors(List<SensorTypeDTO> mandatorySensors) {
        this.mandatorySensors = mandatorySensors;
    }
}
