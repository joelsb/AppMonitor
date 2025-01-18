package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;

import java.util.List;

@Stateless
public class ProductRecordBean {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductRecord create(Long productTypeId, Integer quantity, Long volumeId) throws MyIllegalArgumentException {
        if(productTypeId == null || productTypeId <= 0) throw new MyIllegalArgumentException("ProductType id must be greater than 0");
        var productType = entityManager.find(ProductType.class, productTypeId);
        if(productType == null) {
            throw new MyEntityNotFoundException("ProductType with id: '" + productTypeId + "' not found");
        }
        if(volumeId == null || volumeId <= 0) throw new MyIllegalArgumentException("Volume id must be greater than 0");
        var volume = entityManager.find(Volume.class, volumeId);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume with id: '" + volumeId + "' not found");
        }
        if(quantity == null || quantity <= 0) throw new MyIllegalArgumentException("Quantity must be greater than 0");
        var productRecord = new ProductRecord(productType, quantity, volume);
        productType.addProductRecord(productRecord);
        volume.addProduct(productRecord);
        entityManager.persist(productRecord);
        return productRecord;
    }

    public ProductRecord find(Long id) {
        if(id == null || id <= 0) throw new RuntimeException("ProductRecord id must be greater than 0");
        var productRecord = entityManager.find(ProductRecord.class, id);
        if (productRecord == null) {
            throw new MyEntityNotFoundException("ProductRecord with id: '" + id + "' not found");
        }
        return productRecord;
    }

    public List<ProductRecord> findAll() {
        return entityManager.createNamedQuery("getAllProductRecords", ProductRecord.class).getResultList();
    }

    public ProductRecord update(Long id, Long productTypeId, Integer quantity, Long volumeId) throws MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("ProductRecord id must be greater than 0");
        var productRecord = find(id);
        if(productTypeId == null || productTypeId <= 0) throw new MyIllegalArgumentException("ProductType id must be greater than 0");
        var productType = entityManager.find(ProductType.class, productTypeId);
        if(productType == null) {
            throw new MyEntityNotFoundException("ProductType with id: '" + productTypeId + "' not found");
        }
        if(volumeId == null || volumeId <= 0) throw new MyIllegalArgumentException("Volume id must be greater than 0");
        var volume = entityManager.find(Volume.class, volumeId);
        if(volume == null) throw new MyEntityNotFoundException("Volume with id: '" + volumeId + "' not found");
        productRecord.getProduct().removeProductRecord(productRecord);
        productRecord.setProduct(productType);
        productType.addProductRecord(productRecord);
        if(quantity == null || quantity <= 0) throw new MyIllegalArgumentException("Quantity must be greater than 0");
        productRecord.setQuantity(quantity);
        productRecord.getVolume().removeProduct(productRecord);
        productRecord.setVolume(volume);
        volume.addProduct(productRecord);
        entityManager.merge(productRecord);
        return productRecord;
    }

    public void delete(Long id) throws MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("ProductRecord id must be greater than 0");
        var productRecord = find(id);
        entityManager.remove(productRecord);
    }
}
