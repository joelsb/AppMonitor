package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class ProductType extends Versionable{
    /*
    id é criado pelo sistema
    name-String
    mandatoryPackage-boolean
    mandatorySensors-List<SensorType>
    volume-Volume
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private boolean mandatoryPackage;
    @NotNull
    private List<SensorType> mandatorySensors;
    @NotNull
    private Volume volume;

    public ProductType() {
    }

    public ProductType(String name, boolean mandatoryPackage, List<SensorType> mandatorySensors, Volume volume) {
        this.name = name;
        this.mandatoryPackage = mandatoryPackage;
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

    public boolean isMandatoryPackage() {
        return mandatoryPackage;
    }

    public void setMandatoryPackage(boolean mandatoryPackage) {
        this.mandatoryPackage = mandatoryPackage;
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
