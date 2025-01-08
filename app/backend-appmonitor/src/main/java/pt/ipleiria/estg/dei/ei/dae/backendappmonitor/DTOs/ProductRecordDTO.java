package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductRecord;

import java.util.List;
import java.util.stream.Collectors;

public class ProductRecordDTO {
    /*
    id: long
    product: ProductType
    quantity: long
    volume: Volume
     */

    public Long id;
    public long productId;
    public int quantity;
    public Long volumeId;
    public String productName;

    public ProductRecordDTO() {
    }

    public ProductRecordDTO(Long id, long productId, int quantity, Long volumeId, String productName) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.volumeId = volumeId;
        this.productName = productName;
    }

    public static ProductRecordDTO from(ProductRecord productRecord) {
        return new ProductRecordDTO(
                null,
                productRecord.getProduct().getId(),
                productRecord.getQuantity(),
                null,
                productRecord.getProduct().getName()
        );
    }

    public static List<ProductRecordDTO> from(List<ProductRecord> productRecords) {
        return productRecords.stream().map(ProductRecordDTO::from).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }

    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}
}
