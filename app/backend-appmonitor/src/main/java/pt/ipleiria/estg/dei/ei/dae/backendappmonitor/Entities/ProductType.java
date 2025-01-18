package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "productTypes",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")}
)
@NamedQueries(
        {
                @NamedQuery(
                        name = "getAllProductTypes",
                        query = "SELECT pt FROM ProductType pt ORDER BY pt.id, pt.name"
                ),
                @NamedQuery(
                        name = "getProductTypeByName",
                        query = "SELECT pt FROM ProductType pt WHERE pt.name = :name ORDER BY pt.id, pt.name"
                )
        }
)

@Entity
public class ProductType extends Versionable implements Serializable {
    @Id
    private long id;
    @NotNull
    private String name;
    @NotNull
    private boolean mandatoryPackage;
    @NotNull
    @ManyToMany
    private List<SensorType> mandatorySensors = new ArrayList<>();
    @OneToMany(mappedBy = "product")
    private List<ProductRecord> productRecords = new ArrayList<>();

    public ProductType() {
    }

    public ProductType(long id, String name, boolean mandatoryPackage) {
        this.id = id;
        this.name = name;
        this.mandatoryPackage = mandatoryPackage;
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
        return new ArrayList<>(mandatorySensors);
    }

    public void addMandatorySensor(SensorType sensor) {
        mandatorySensors.add(sensor);
    }

    public void removeMandatorySensor(SensorType sensor) {
        mandatorySensors.remove(sensor);
    }

    public List<ProductRecord> getProductRecords() {
        return new ArrayList<>(productRecords);
    }

    public void addProductRecord(ProductRecord productRecord) {
        productRecords.add(productRecord);
    }

    public void removeProductRecord(ProductRecord productRecord) {
        productRecords.remove(productRecord);
    }
}
