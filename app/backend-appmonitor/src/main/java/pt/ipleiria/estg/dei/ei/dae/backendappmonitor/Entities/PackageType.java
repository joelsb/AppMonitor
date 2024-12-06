package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "packageTypes",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@NamedQueries({
        @NamedQuery(name = "getAllPackageTypes",
                query = "SELECT pt FROM PackageType pt ORDER BY pt.id, pt.name"),
        @NamedQuery(name = "getPackageTypeByName",
                query = "SELECT pt FROM PackageType pt WHERE pt.name = :name ORDER BY pt.id, pt.name")
})

@Entity
public class PackageType extends Versionable implements Serializable {
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
    @ManyToMany
    @JoinTable(name = "packageType_sensorType",
            joinColumns = @JoinColumn(name = "packageType_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sensorType_id", referencedColumnName = "id"))
    private List<SensorType> mandatorySensors;
    @OneToMany(mappedBy = "packageType")
    private List<Volume> volumes;

    public PackageType() {
    }

    public PackageType(String name) {
        this.name = name;
        this.mandatorySensors = new ArrayList<>();
        this.volumes = new ArrayList<>();
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
        return new ArrayList<>(mandatorySensors);
    }

    public void addMandatorySensor(SensorType sensor) {
        mandatorySensors.add(sensor);
    }

    public void removeMandatorySensor(SensorType sensor) {
        mandatorySensors.remove(sensor);
    }

    public List<Volume> getVolume() {
        return new ArrayList<>(volumes);
    }

    public void addVolume(Volume volume) {
        volumes.add(volume);
    }

    public void removeVolume(Volume volume) {
        volumes.remove(volume);
    }

}
