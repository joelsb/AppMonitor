package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

public class SensorTypeCreateDTO {
    private long id;
    private String name;
    private String unit;
    private ProductTypeDTO productTypeDTO;
    private SensorDTO sensorDTO;
    private PackageTypeDTO packageTypeDTO;
    private double ceiling;
    private double floor;

    public SensorTypeCreateDTO() {
    }

    public SensorTypeCreateDTO(long id, String name, String unit, ProductTypeDTO productTypeDTO, SensorDTO sensorDTO, PackageTypeDTO packageTypeDTO, double ceiling, double floor) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.productTypeDTO = productTypeDTO;
        this.sensorDTO = sensorDTO;
        this.packageTypeDTO = packageTypeDTO;
        this.ceiling = ceiling;
        this.floor = floor;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ProductTypeDTO getProductTypeDTO() {
        return productTypeDTO;
    }

    public void setProductTypeDTO(ProductTypeDTO productTypeDTO) {
        this.productTypeDTO = productTypeDTO;
    }

    public SensorDTO getSensorDTO() {
        return sensorDTO;
    }

    public void setSensorDTO(SensorDTO sensorDTO) {
        this.sensorDTO = sensorDTO;
    }

    public PackageTypeDTO getPackageTypeDTO() {
        return packageTypeDTO;
    }

    public void setPackageTypeDTO(PackageTypeDTO packageTypeDTO) {
        this.packageTypeDTO = packageTypeDTO;
    }

    public double getCeiling() {
        return ceiling;
    }

    public void setCeiling(double ceiling) {
        this.ceiling = ceiling;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }
}
