package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.ejb.EJB;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedQueries(
        {
                @NamedQuery(name = "getAllOrders", query = "SELECT o FROM Order o ORDER BY o.createdDate"),
                @NamedQuery(name = "getOrdersByCustomer", query = "SELECT o FROM Order o WHERE o.customer = :customer ORDER BY o.createdDate"),
                @NamedQuery(name = "getAvailableOrders", query = "SELECT o FROM Order o WHERE o.deliveredDate = null ORDER BY o.createdDate"),
        }
)


public class Order extends Versionable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private Date createdDate;
    private Date deliveredDate;
    @NotNull
    @ManyToOne
    private Customer customer;
    @NotNull
    @OneToMany(mappedBy = "order")
    private List<Volume> volumes = new ArrayList<>();


    public Order() {
    }

    public Order( Date createdDate, Customer customer) {
        this.createdDate = createdDate;
        this.deliveredDate = null;
        this.customer = customer;
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
