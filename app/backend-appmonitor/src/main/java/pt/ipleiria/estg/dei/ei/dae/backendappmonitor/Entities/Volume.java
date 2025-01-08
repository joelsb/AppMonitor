package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "volumes")
@NamedQueries(
        {
                @NamedQuery(name = "getAllVolumes", query = "SELECT v FROM Volume v ORDER BY v.id, v.sentDate"),
                @NamedQuery(name = "getVolumesByOrder", query = "SELECT v FROM Volume v WHERE v.order = :order ORDER BY v.id, v.sentDate")
        }
)
@Entity
public class Volume extends Versionable implements Serializable {
    @Id
    private long id;
    @NotNull
    private Date sentDate;
    private Date deliveredDate;

    @ManyToOne
    private PackageType packageType;
    @NotNull
    @OneToMany(mappedBy = "volume")
    private List<ProductRecord> products = new ArrayList<>();
    @NotNull
    @OneToMany(mappedBy = "volume")
    private List<Sensor> sensors = new ArrayList<>();
    @NotNull
    @ManyToOne
    private Order order;

    public Volume() {
    }

    public Volume(long id,Date sentDate, PackageType packageType, Order order) {
        this.id=id;
        this.sentDate = sentDate;
        this.deliveredDate = null;
        this.packageType = packageType;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType pack) {
        this.packageType = pack;
    }

    public List<ProductRecord> getProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(ProductRecord product) {
        products.add(product);
    }

    public void removeProduct(ProductRecord product) {
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
