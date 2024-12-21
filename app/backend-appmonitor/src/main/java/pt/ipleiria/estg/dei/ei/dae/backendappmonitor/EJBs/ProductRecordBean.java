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

    public ProductRecord create(ProductType product, int quantity, Volume volume) {
        var productRecord = new ProductRecord(product, quantity, volume);
        entityManager.persist(productRecord);
        return productRecord;
    }
}
