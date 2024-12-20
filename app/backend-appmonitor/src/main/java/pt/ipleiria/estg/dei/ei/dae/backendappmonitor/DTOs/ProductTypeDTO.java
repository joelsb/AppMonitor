package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import jakarta.json.bind.annotation.*;
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

    @JsonbProperty("id") // Will serialize first
    private long id;

    @JsonbProperty("name") // Will serialize second
    private String name;

    @JsonbProperty("mandatoryPackage") // Will serialize third
    private boolean mandatoryPackage;

    @JsonbProperty("mandatorySensors") // Will serialize fourth
    private List<SensorTypeDTO> mandatorySensors;

    private boolean excludeMandatorySensors; // Flag to control serialization

    public ProductTypeDTO() {
    }

    public ProductTypeDTO(long id, String name, boolean mandatoryPackage) {
        this.id = id;
        this.name = name;
        this.mandatoryPackage = mandatoryPackage;
        this.mandatorySensors = new ArrayList<>();
        this.excludeMandatorySensors = false;
    }

    public static ProductTypeDTO from(ProductType productType) {
        return new ProductTypeDTO(
                productType.getId(),
                productType.getName(),
                productType.isMandatoryPackage()
        );
    }

    public static List<ProductTypeDTO> from(List<ProductType> productTypes) {
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
        return excludeMandatorySensors ? null : new ArrayList<>(mandatorySensors);
    }

    public void setMandatorySensors(List<SensorTypeDTO> mandatorySensors) {
        this.mandatorySensors = mandatorySensors;
    }

    public void setExcludeMandatorySensors(boolean excludeMandatorySensors) {
        this.excludeMandatorySensors = excludeMandatorySensors;
    }
}
