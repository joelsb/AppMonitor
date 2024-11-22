package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SensorType extends Versionable{
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
    private List<ProductType> products;
    @NotNull
    private List<Sensor> sensors;
    @NotNull
    private List<PackageType> packageTypes;

    public SensorType() {
    }


    public SensorType(String name, String unit) {
        this.name = name;
        this.unit = unit;
        this.products = new ArrayList<>();
        this.sensors = new ArrayList<>();
        this.packageTypes = new ArrayList<>();
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

    public List<ProductType> getProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(ProductType product) {
        products.add(product);
    }

    public void removeProduct(ProductType product) {
        products.remove(product);
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

}
