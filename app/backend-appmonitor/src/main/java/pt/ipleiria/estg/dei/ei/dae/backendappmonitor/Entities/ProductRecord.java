package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;

@Entity
public class ProductRecord extends Versionable{
    /*
    Id é criado pelo sistema
    id-long
    product-ProductType
    quantity-int
    volume-Volume
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private ProductType product;
    private int quantity;
    private Volume volume;

    public ProductRecord() {
    }

    public ProductRecord(ProductType product, int quantity, Volume volume) {
        this.product = product;
        this.quantity = quantity;
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public ProductType getProduct() {
        return product;
    }

    public void setProduct(ProductType product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

}
