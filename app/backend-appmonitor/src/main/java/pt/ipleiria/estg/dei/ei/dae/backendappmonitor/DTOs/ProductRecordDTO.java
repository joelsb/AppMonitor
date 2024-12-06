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

    public long id;
    public long productId;
    public int quantity;
    public long volumeId;

    public ProductRecordDTO() {
    }

    public ProductRecordDTO(long id, long productId, int quantity, long volumeId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.volumeId = volumeId;
    }

    public static ProductRecordDTO from(ProductRecord productRecord) {
        return new ProductRecordDTO(
                productRecord.getId(),
                productRecord.getProduct().getId(),
                productRecord.getQuantity(),
                productRecord.getVolume().getId()
        );
    }

    public static List<ProductRecordDTO> from(List<ProductRecord> productRecords) {
        return productRecords.stream().map(ProductRecordDTO::from).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(long volumeId) {
        this.volumeId = volumeId;
    }
}
