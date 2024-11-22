package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Volume extends Versionable {
    @Id
    private long id;
    @NotNull
    private Date sentDate;
    private Date deliveredDate;
    private PackageType pack;
    @NotNull
    private List<ProductRecord> products;
    @NotNull
    private List<Sensor> sensors;
    @NotNull
    private Order order;

    public Volume() {
    }

    public Volume(long id, Date sentDate, PackageType pack, List<ProductRecord> products, List<Sensor> sensors, Order order) {
        this.id = id;
        this.sentDate = sentDate;
        this.deliveredDate = null;
        this.pack = pack;
        this.products = products;
        this.sensors = sensors;
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

    public PackageType getPack() {
        return pack;
    }

    public void setPack(PackageType pack) {
        this.pack = pack;
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
