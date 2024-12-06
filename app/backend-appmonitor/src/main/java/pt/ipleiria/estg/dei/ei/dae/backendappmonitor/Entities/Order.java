package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedQueries(
        {
                @NamedQuery(name = "getAllOrders", query = "SELECT o FROM Order o ORDER BY o.id, o.createdDate"),
                @NamedQuery(name = "getOrdersByCustomer", query = "SELECT o FROM Order o WHERE o.customer = :customer ORDER BY o.id, o.createdDate")
        }
)
@Table(name = "orders")
@Entity
public class Order extends Versionable implements Serializable {
    @Id
    private long id;
    @NotNull
    private Date createdDate;
    private Date deliveredDate;
    @NotNull
    @ManyToOne
    private Customer customer;
    @NotNull
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Volume> volumes;


    public Order() {
    }

    public Order(long id, Date createdDate, Date deliveredDate, Customer customer) {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date creationDate) {
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

    public void addVolume(Volume volume) {
        volumes.add(volume);
    }

    public void removeVolume(Volume volume) {
        volumes.remove(volume);
    }
}
