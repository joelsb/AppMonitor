package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductType find(Long id) throws MyEntityNotFoundException {
        var productType = entityManager.find(ProductType.class, id);
        if (productType == null) {
            throw new MyEntityNotFoundException("ProductType (" + id + ") not found");
        }
        return productType;
    }

    public List<ProductType> findAll() {
        // remember, maps to: “SELECT a FROM User a ORDER BY a.name”
        return entityManager.createNamedQuery("getAllProductTypes", ProductType.class).getResultList();
    }

    public ProductType findWithMandatorySensors(Long id) throws MyEntityNotFoundException {
        var productType = this.find(id);
        //Initialize the lazy collection
        Hibernate.initialize(productType.getMandatorySensors());
        return productType;
    }

    public List<ProductType> findAllWithMandatorySensors() {
        var productTypes = this.findAll();
        productTypes.forEach(productType -> Hibernate.initialize(productType.getMandatorySensors()));
        return productTypes;
    }

    public ProductType create(String name, boolean mandatoryPackage) {
        var productType = new ProductType(name, mandatoryPackage);
        entityManager.persist(productType);
        return productType;
    }

    public ProductType update(Long id, String name, boolean mandatoryPackage) throws MyEntityNotFoundException {
        var productType = entityManager.find(ProductType.class, id);
        if (productType == null) {
            throw new MyEntityNotFoundException("ProductType (" + id + ") not found");
        }
        productType.setName(name);
        productType.setMandatoryPackage(mandatoryPackage);
        return productType;
    }


}
