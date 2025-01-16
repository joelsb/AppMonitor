package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;

@Stateless
public class ProductRecordBean {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductRecord create(long productTypeId, int quantity, long volumeId) {
        var productType = entityManager.find(ProductType.class, productTypeId);
        if(productType == null) {
            throw new RuntimeException("ProductType with id: '" + productTypeId + "' not found");
        }
        var volume = entityManager.find(Volume.class, volumeId);
        if(volume == null) {
            throw new RuntimeException("Volume with id: '" + volumeId + "' not found");
        }
        if(quantity <= 0) throw new RuntimeException("Quantity must be greater than 0");
        var productRecord = new ProductRecord(productType, quantity, volume);
        productType.addProductRecord(productRecord);
        volume.addProduct(productRecord);
        entityManager.persist(productRecord);
        return productRecord;
    }

    public ProductRecord find(long id) {
        var productRecord = entityManager.find(ProductRecord.class, id);
        if (productRecord == null) {
            throw new RuntimeException("ProductRecord with id: '" + id + "' not found");
        }
        return productRecord;
    }

    public ProductRecord update(long id, long productTypeId, int quantity, long volumeId) {
        var productRecord = find(id);
        var productType = entityManager.find(ProductType.class, productTypeId);
        if(productType == null) {
            throw new RuntimeException("ProductType with id: '" + productTypeId + "' not found");
        }
        var volume = entityManager.find(Volume.class, volumeId);
        if(volume == null) {
            throw new RuntimeException("Volume with id: '" + volumeId + "' not found");
        }
        productRecord.getProduct().removeProductRecord(productRecord);
        productRecord.setProduct(productType);
        productType.addProductRecord(productRecord);
        if(quantity <= 0) throw new RuntimeException("Quantity must be greater than 0");
        productRecord.setQuantity(quantity);
        productRecord.getVolume().removeProduct(productRecord);
        productRecord.setVolume(volume);
        volume.addProduct(productRecord);
        entityManager.merge(productRecord);
        return productRecord;
    }

    public void delete(long id) {
        var productRecord = find(id);
        entityManager.remove(productRecord);
    }
}
