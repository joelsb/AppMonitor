package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

public class ProductTypeCreateDTO {
    private long id;
    private String name;
    private boolean mandatoryPackage;
    private SensorDTO sensor;
    private ProductRecordDTO productRecord;

    public ProductTypeCreateDTO() {
    }
    public ProductTypeCreateDTO(long id, String name, boolean mandatoryPackage, SensorDTO sensor, ProductRecordDTO productRecord) {
        this.id = id;
        this.name = name;
        this.mandatoryPackage = mandatoryPackage;
        this.sensor = sensor;
        this.productRecord = productRecord;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isMandatoryPackage() {
        return mandatoryPackage;
    }
    public void setMandatoryPackage(boolean mandatoryPackage) {
        this.mandatoryPackage = mandatoryPackage;
    }
    public SensorDTO getSensor() {
        return sensor;
    }
    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
    public ProductRecordDTO getProductRecord() {
        return productRecord;
    }
    public void setProductRecord(ProductRecordDTO productRecord) {
        this.productRecord = productRecord;
    }

}
