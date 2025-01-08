package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeCreateDTO {
    private long id;
    private Date sentDate;
    private Date deliveredDate;
    private Long orderId;
    private long packageTypeId;
    private List<ProductRecordDTO> products = new ArrayList<>();
    private List<SensorDTO> sensors = new ArrayList<>();

    public VolumeCreateDTO() {
    }

    public VolumeCreateDTO(long id, Date sentDate, Date deliveredDate, Long orderId, long packageTypeId) {
        this.id = id;
        this.sentDate = sentDate;
        this.deliveredDate = deliveredDate;
        this.orderId = orderId;
        this.packageTypeId = packageTypeId;
    }

    public static VolumeCreateDTO from(Volume volume) {
        return new VolumeCreateDTO(
                volume.getId(),
                volume.getSentDate(),
                null,
                null,
                volume.getPackageType().getId());
    }

    public static List<VolumeCreateDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeCreateDTO::from).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public long getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(long packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public List<ProductRecordDTO> getProducts() {
        return new ArrayList<>(products);
    }

    public void setProducts(List<ProductRecordDTO> products) {
        this.products = products;
    }

    public List<SensorDTO> getSensors() {
        return new ArrayList<>(sensors);
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }
}
