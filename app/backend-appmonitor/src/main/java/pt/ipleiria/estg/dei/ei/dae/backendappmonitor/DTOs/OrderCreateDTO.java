package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderCreateDTO {

    private long id;
    private String customerUsername;
    private Date createdDate;
    private VolumeDTO volume;

    public OrderCreateDTO() {
    }

    public OrderCreateDTO(long id, String customerUsername, Date createdDate, VolumeDTO volume) {
        this.id = id;
        this.customerUsername = customerUsername;
        this.createdDate = createdDate;
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public VolumeDTO getVolume() {
        return volume;
    }

    public void setVolume(VolumeDTO volume) {
        this.volume = volume;
    }
}
