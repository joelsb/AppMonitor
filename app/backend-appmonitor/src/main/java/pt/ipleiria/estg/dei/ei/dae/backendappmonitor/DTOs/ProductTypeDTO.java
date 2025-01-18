package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.json.bind.annotation.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTypeDTO {
    private Long id;

    private String name;

    private Boolean mandatoryPackage;

    private List<SensorTypeDTO> mandatorySensors = new ArrayList<>();

    public ProductTypeDTO() {
    }

    public ProductTypeDTO(Long id, String name, Boolean mandatoryPackage) {
        this.id = id;
        this.name = name;
        this.mandatoryPackage = mandatoryPackage;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isMandatoryPackage() {
        return mandatoryPackage;
    }

    public void setMandatoryPackage(Boolean mandatoryPackage) {
        this.mandatoryPackage = mandatoryPackage;
    }

    public List<SensorTypeDTO> getMandatorySensors() {
        return mandatorySensors.isEmpty() ? null : new ArrayList<>(mandatorySensors);
    }

    public void setMandatorySensors(List<SensorTypeDTO> mandatorySensors) {
        this.mandatorySensors = mandatorySensors;
    }
}
