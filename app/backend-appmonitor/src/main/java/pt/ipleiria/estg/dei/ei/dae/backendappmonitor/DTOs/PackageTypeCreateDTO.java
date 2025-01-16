package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageTypeCreateDTO {
    private long id;
    private String name;
    private SensorDTO sensor;
    private VolumeDTO volume;

    public PackageTypeCreateDTO() {
    }
    public PackageTypeCreateDTO(long id, String name, SensorDTO sensor, VolumeDTO volume) {
        this.id = id;
        this.name = name;
        this.sensor = sensor;
        this.volume = volume;
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
    public SensorDTO getSensor() {
        return sensor;
    }
    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
    public VolumeDTO getVolume() {
        return volume;
    }
    public void setVolume(VolumeDTO volume) {
        this.volume = volume;
    }

}
