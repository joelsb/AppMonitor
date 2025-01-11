package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductRecord;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRecordDTO {
    /*
    id: long
    product: ProductType
    quantity: long
    volume: Volume
     */

    public Long id;
    public Long productId;
    public String productName;
    public Integer quantity;
    public Long volumeId;

    public ProductRecordDTO() {
    }

    public ProductRecordDTO(Long id, Long productId, Integer quantity, Long volumeId, String productName) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.volumeId = volumeId;
        this.productName = productName;
    }

    public static ProductRecordDTO from(ProductRecord productRecord) {
        return new ProductRecordDTO(
                productRecord.getId(),
                productRecord.getProduct().getId(),
                productRecord.getQuantity(),
                productRecord.getVolume().getId(),
                productRecord.getProduct().getName()
        );
    }

    public static ProductRecordDTO fromSimple(ProductRecord productRecord) {
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
    public static List<ProductRecordDTO> fromSimple(List<ProductRecord> productRecords) {
        return productRecords.stream().map(ProductRecordDTO::fromSimple).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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
