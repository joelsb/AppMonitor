package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderCreateDTO {

    private long id;
    private String customerUsername;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Your/Timezone")
    private Date createdDate;
    private VolumeCreateDTO volume;

    public OrderCreateDTO() {
    }

    public OrderCreateDTO(long id, String customerUsername, Date createdDate, VolumeCreateDTO volume) {
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

    public VolumeCreateDTO getVolume() {
        return volume;
    }

    public void setVolume(VolumeCreateDTO volume) {
        this.volume = volume;
    }
}
