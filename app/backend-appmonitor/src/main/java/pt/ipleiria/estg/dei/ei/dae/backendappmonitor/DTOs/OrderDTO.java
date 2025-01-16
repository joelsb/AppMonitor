package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Your/Timezone")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Your/Timezone")
    private Date  deliveredDate;
    private String customerUsername;
    private List<VolumeDTO> volumes = new ArrayList<>();
    private List<Long> volumesIds = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(Long id, Date createdDate, Date deliveredDate, String customerUsername) {
        this.id = id;
        this.createdDate = createdDate;
        this.deliveredDate = deliveredDate;
        this.customerUsername = customerUsername;
    }

    public static OrderDTO from(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCreatedDate(),
                order.getDeliveredDate(),
                order.getCustomer().getUsername()
        );
    }

    public static OrderDTO fromCustomer(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCreatedDate(),
                order.getDeliveredDate(),
                null
        );
    }


    public static List<OrderDTO> fromCustomer(List<Order> orders) {
        return orders.stream().map(OrderDTO::fromCustomer).collect(Collectors.toList());
    }

    public static List<OrderDTO> from(List<Order> orders) {
        return orders.stream().map(OrderDTO::from).collect(Collectors.toList());
    }

    public static Long getId(Order order) {
        return order.getId();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public List<VolumeDTO> getVolumes() {
        return volumes.isEmpty() ? null : new ArrayList<>(volumes);
    }

    public void setVolumes(List<VolumeDTO> volumes) {
        this.volumes = volumes;
    }

    public List<Long> getVolumesIds() {
        return volumesIds.isEmpty() ? null : new ArrayList<>(volumesIds);
    }

    public void setVolumesIds(List<Long> volumesIds) {
        this.volumesIds = volumesIds;
    }
}
