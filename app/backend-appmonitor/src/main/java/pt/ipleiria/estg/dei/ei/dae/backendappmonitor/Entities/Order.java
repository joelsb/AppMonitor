package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Order extends Versionable {
    private long id;
    private Date createdDate;
    private Date deliveredDate;
    private Customer customer;
    private List<Volume> volumes;


    public Order() {
    }

    public Order(long id, Date createdDate, Date deliveredDate, Customer customer, List<Volume> volumes) {
        this.id = id;
        this.createdDate = createdDate;
        this.deliveredDate = deliveredDate;
        this.customer = customer;
        this.volumes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return createdDate;
    }

    public void setCreationDate(Date creationDate) {
        this.createdDate = creationDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Volume> getVolumes() {
        return new ArrayList<>(volumes);
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }
}
