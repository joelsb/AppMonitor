package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Utils;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Utils.VolumeValidationResult;

import java.util.ArrayList;
import java.util.List;

public class OrderValidationResult {
    private Customer customer;
    private PackageType packageType;
    private List<SensorType> sensorTypes = new ArrayList<>();
    private List<ProductType> productTypes = new ArrayList<>();

    public OrderValidationResult() {
    }

    public OrderValidationResult(Customer customer, PackageType packageType, List<SensorType> sensorTypes, List<ProductType> productTypes) {
        this.customer = customer;
        this.packageType = packageType;
        this.sensorTypes = sensorTypes;
        this.productTypes = productTypes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public List<SensorType> getSensorTypes() {
        return sensorTypes;
    }

    public void setSensorTypes(List<SensorType> sensorTypes) {
        this.sensorTypes = sensorTypes;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }
}