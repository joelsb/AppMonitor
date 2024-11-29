package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductTypeDTO {
    /*
    id: long
    name: String
    mandatoryPackage: boolean
    mandatorySensors: List<SensorType>
    productRecords: List<ProductRecord>
     */

    private long id;
    private String name;
    private boolean mandatoryPackage;
    private List<SensorTypeDTO> mandatorySensors;
    private List<ProductRecordDTO> productRecords;

    public ProductTypeDTO() {
    }

    public ProductTypeDTO(long id, String name, boolean mandatoryPackage) {
        this.id = id;
        this.name = name;
        this.mandatoryPackage = mandatoryPackage;
        this.mandatorySensors = new ArrayList<>();
        this.productRecords = new ArrayList<>();
    }

    public static ProductTypeDTO from(ProductType productType) {
        return new ProductTypeDTO(
                productType.getId(),
                productType.getName(),
                productType.isMandatoryPackage()
        );
    }

    public List<ProductTypeDTO> from(List<ProductType> productTypes) {
        return productTypes.stream().map(ProductTypeDTO::from).collect(Collectors.toList());
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

    public List<SensorTypeDTO> getMandatorySensors() {
        return mandatorySensors;
    }

    public void setMandatorySensors(List<SensorTypeDTO> mandatorySensors) {
        this.mandatorySensors = mandatorySensors;
    }

    public List<ProductRecordDTO> getProductRecords() {
        return productRecords;
    }

    public void setProductRecords(List<ProductRecordDTO> productRecords) {
        this.productRecords = productRecords;
    }
}
