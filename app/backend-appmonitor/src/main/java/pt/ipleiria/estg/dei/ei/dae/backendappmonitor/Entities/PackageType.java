package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

public class PackageType extends Versionable{
    /*
    Id é criado pelo sistema
    name-String
    mandatorySensors-List<SensorType>
    volume-Volume
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private List<SensorType> mandatorySensors;
    @NotNull
    private Volume volume;

    public PackageType() {
    }

    public PackageType(String name, List<SensorType> mandatorySensors, Volume volume) {
        this.name = name;
        this.mandatorySensors = mandatorySensors;
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SensorType> getMandatorySensors() {
        return mandatorySensors;
    }

    public void addMandatorySensor(SensorType sensor) {
        mandatorySensors.add(sensor);
    }

    public void removeMandatorySensor(SensorType sensor) {
        mandatorySensors.remove(sensor);
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

}
