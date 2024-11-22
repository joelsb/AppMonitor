package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import java.util.ArrayList;
import java.util.List;

public class PackageType extends Versionable{
    private long id;
    private String name;
    private List<SensorType> mandatorySensors;
    private Volume volume;


    public PackageType() {
    }

    public PackageType(long id, String name, List<SensorType> mandatorySensors, Volume volume) {
        this.id = id;
        this.name = name;
        this.mandatorySensors = new ArrayList<>();
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

    public List<SensorType> getMandatorySensors() {
        return new ArrayList<>(mandatorySensors);

    }

    public void setMandatorySensors(List<SensorType> mandatorySensors) {
        this.mandatorySensors = mandatorySensors;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }
}
