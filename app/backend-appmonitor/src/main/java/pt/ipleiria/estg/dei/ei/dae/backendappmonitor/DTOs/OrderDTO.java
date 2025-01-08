package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    private long id;
    private Date createdDate;
    private Date  deliveredDate;
    private String customerUsername;
    private List<VolumeDTO> volumes;

    public OrderDTO() {
    }

    public OrderDTO(long id, Date createdDate, Date deliveredDate, String customerUsername) {
        this.id = id;
        this.createdDate = createdDate;
        this.deliveredDate = deliveredDate;
        this.customerUsername = customerUsername;
        this.volumes = new ArrayList<>();
    }

    public static OrderDTO from(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCreatedDate(),
                order.getDeliveredDate(),
                null
        );
    }

    public static OrderDTO fromManager(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCreatedDate(),
                order.getDeliveredDate(),
                order.getCustomer().getUsername()
        );
    }


    public static List<OrderDTO> fromManager(List<Order> orders) {
        return orders.stream().map(OrderDTO::fromManager).collect(Collectors.toList());
    }

    public static List<OrderDTO> from(List<Order> orders) {
        return orders.stream().map(OrderDTO::from).collect(Collectors.toList());
    }

    public static long getId(Order order) {
        return order.getId();
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
        return new ArrayList<>(volumes);
    }

    public void setVolumes(List<VolumeDTO> volumes) {
        this.volumes = volumes;
    }
}
