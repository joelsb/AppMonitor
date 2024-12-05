package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "sensorTypes")
@NamedQueries(
        {
                @NamedQuery(
                        name = "getAllSensorTypes",
                        query = "SELECT st FROM SensorType st ORDER BY st.name"
                ),
                @NamedQuery(
                        name = "getSensorTypeByName",
                        query = "SELECT st FROM SensorType st WHERE st.name = :name"
                )
        }
)

@Entity
public class SensorType extends Versionable implements Serializable {
    /*
    Id é criado pelo sistema
    name-String
    unit-String
    porducts-List<ProductType>
    sensors-List<Sensor>
    packageTypes-List<PackageType>
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String unit;
    @NotNull
    @ManyToMany
    @JoinTable(name = "sensorType_productType",
            joinColumns = @JoinColumn(name = "sensorType_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "productType_id", referencedColumnName = "id"))
    private List<ProductType> productTypes;
    @NotNull
    @OneToMany(mappedBy = "sensorType")
    private List<Sensor> sensors;
    @NotNull
    @ManyToMany
    private List<PackageType> packageTypes;
    private double ceiling;
    private double floor;

    public SensorType() {
    }


    public SensorType(String name, String unit, double ceiling, double floor) {
        this.name = name;
        this.unit = unit;
        this.productTypes = new ArrayList<>();
        this.sensors = new ArrayList<>();
        this.packageTypes = new ArrayList<>();
        this.ceiling = ceiling;
        this.floor = floor;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<ProductType> getProductTypes() {
        return new ArrayList<>(productTypes);
    }

    public void addProductType(ProductType productType) {
        productTypes.add(productType);
    }

    public void removeProductType(ProductType productType) {
        productTypes.remove(productType);
    }

    public List<Sensor> getSensors() {
        return new ArrayList<>(sensors);
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
    }

    public List<PackageType> getPackageTypes() {
        return new ArrayList<>(packageTypes);
    }

    public void addPackageType(PackageType packageType) {
        packageTypes.add(packageType);
    }

    public void removePackageType(PackageType packageType) {
        packageTypes.remove(packageType);
    }

    public double getCeiling() {
        return ceiling;
    }

    public void setCeiling(double ceiling) {
        this.ceiling = ceiling;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }
}
