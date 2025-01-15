package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductTypeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductType find(Long id) throws MyEntityNotFoundException {
        var productType = entityManager.find(ProductType.class, id);
        if (productType == null) {
            throw new MyEntityNotFoundException("ProductType with id: '" + id + "' not found");
        }
        return productType;
    }

    public ProductType findByName(String name) {
        return entityManager.createNamedQuery("getProductTypeByName", ProductType.class)
                .setParameter("name", name)
                .getSingleResult();
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

    public ProductType create(long id, String name, boolean mandatoryPackage) throws MyEntityExistsException {
        if(!entityManager.createNamedQuery("getProductTypeByName", ProductType.class)
                .setParameter("name", name)
                .getResultList().isEmpty()) {
            throw new MyEntityExistsException("ProductType with name: '" + name + "' already exists");
        }
        if(entityManager.find(ProductType.class, id) != null){
            throw new MyEntityExistsException("ProductType with id: '" + id + "' already exists");
        }
        var productType = new ProductType(id, name, mandatoryPackage);
        entityManager.persist(productType);
        return productType;
    }

    public ProductType create(ProductTypeCreateDTO productTypeCreateDTO) throws MyEntityExistsException {
        if(!entityManager.createNamedQuery("getProductTypeByName", ProductType.class)
                .setParameter("name", productTypeCreateDTO.getName())
                .getResultList().isEmpty()) {
            throw new MyEntityExistsException("ProductType with id: '" + productTypeCreateDTO.getName() + "' already exists");
        }
        var productType = new ProductType(productTypeCreateDTO.getName(), productTypeCreateDTO.isMandatoryPackage());
        entityManager.persist(productType);
        return productType;
    }

    public ProductType update(long id, String name, boolean mandatoryPackage) throws MyEntityNotFoundException, MyEntityExistsException {
        var productType = this.find(id);
        if(!entityManager.createNamedQuery("getProductTypeByName", ProductType.class)
                .setParameter("name", name)
                .getResultList().isEmpty() && !productType.getName().equals(name)) {
            throw new MyEntityExistsException("ProductType with id: '" + name + "' already exists");
        }
        productType.setName(name);
        productType.setMandatoryPackage(mandatoryPackage);
        return productType;
    }

    public void addMandatorySensor(Long id, Long sensorTypeId) throws MyEntityNotFoundException, MyEntityExistsException {
        var productType = this.find(id);
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if (sensorType == null) {
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found");
        }
        if(productType.getMandatorySensors().contains(sensorType)){
            throw new MyEntityExistsException("SensorType with id: '" + sensorTypeId + "' already exists in ProductType with id: '" + id + "'");
        }
        productType.addMandatorySensor(sensorType);
        if(!sensorType.getProductTypes().contains(productType)){
            sensorType.addProductType(productType);
        }
    }

    public ProductType removeMandatorySensor(Long id, Long sensorTypeId) throws MyEntityNotFoundException {
        var productType = this.find(id);
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if (sensorType == null) {
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found");
        }
        if(!productType.getMandatorySensors().contains(sensorType)){
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found in ProductType with id: '" + id + "'");
        }
        productType.removeMandatorySensor(sensorType);
        if(!sensorType.getProductTypes().contains(productType)){
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found in ProductType  with id: '" + id + "'");
        }
        sensorType.removeProductType(productType);
        return productType;
    }
}
