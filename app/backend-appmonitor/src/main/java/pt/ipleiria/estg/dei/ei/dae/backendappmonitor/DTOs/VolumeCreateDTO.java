package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolumeCreateDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Your/Timezone")
    private Date sentDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Your/Timezone")
    private Date deliveredDate;
    private Long orderId;
    private Long packageTypeId;
    private List<ProductRecordDTO> products = new ArrayList<>();
    private List<SensorDTO> sensors = new ArrayList<>();

    public VolumeCreateDTO() {
    }

    public VolumeCreateDTO(Long id, Date sentDate, Date deliveredDate, Long orderId, Long packageTypeId) {
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
                volume.getPackageType() != null ? volume.getPackageType().getId() : null
        );
    }

    public static List<VolumeCreateDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeCreateDTO::from).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Long packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public List<ProductRecordDTO> getProducts() {
        return products.isEmpty() ? null : new ArrayList<>(products);
    }

    public void setProducts(List<ProductRecordDTO> products) {
        this.products = products;
    }

    public List<SensorDTO> getSensors() {
        return sensors.isEmpty() ? null : new ArrayList<>(sensors);
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }
}
