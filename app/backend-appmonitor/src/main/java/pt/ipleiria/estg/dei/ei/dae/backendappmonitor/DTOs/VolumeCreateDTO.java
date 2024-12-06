package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VolumeCreateDTO {
    /*
    Message received:
    "id": 12,
    "sentDate": "2021-06-01T00:00:00",
    "orderId": 26,
    "packageId": 9900,
    "products":
    [
        {
            "productId": 1,
            "quatity": 1
        },
        {
            "productId": 2,
            "quatity": 3
        }
    ],
    "sensors": [
        {
            "id": 1,
            "sensorTypeId": 1
        },
        {
            "id": 2,
            "sensorTypeId": 2
        }
    ]
     */
    private long id;
    private Date sentDate;
    private long orderId;
    private long packageTypeId;
    private List<ProductRecordDTO> products;
    private List<SensorDTO> sensors;

    public VolumeCreateDTO() {
    }

    public VolumeCreateDTO(long id, Date sentDate, long packageTypeId, long orderId) {
        this.id = id;
        this.sentDate = sentDate;
        this.orderId = orderId;
        this.packageTypeId = packageTypeId;
        this.products = new ArrayList<>();
        this.sensors = new ArrayList<>();
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
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
