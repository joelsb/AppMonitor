package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Utils;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;

import java.util.ArrayList;
import java.util.List;

public class VolumeValidationResult {
    private PackageType packageType;
    private List<SensorType> sensorTypes = new ArrayList<>();
    private List<ProductType> productTypes = new ArrayList<>();

    public VolumeValidationResult() {
    }

    public VolumeValidationResult(PackageType packageType, List<SensorType> sensorTypes, List<ProductType> productTypes) {
        this.packageType = packageType;
        this.sensorTypes = sensorTypes;
        this.productTypes = productTypes;
    }

    // Getters and Setters
    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public List<SensorType> getSensorTypes() {
        return new ArrayList<>(sensorTypes);
    }

    public void addSensorType(SensorType sensorType) {
        this.sensorTypes.add(sensorType);
    }

    public List<ProductType> getProductTypes() {
        return new ArrayList<>(productTypes);
    }

    public void addProductType(ProductType productType) {
        this.productTypes.add(productType);
    }
}
