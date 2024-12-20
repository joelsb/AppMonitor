package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;


import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import java.util.List;


@Stateless
public class PackageTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public PackageType find(Long id) throws MyEntityNotFoundException {
        var packageType = entityManager.find(PackageType.class, id);
        if(packageType == null) {
            throw new MyEntityNotFoundException("PackageType (" + id + ") not found");
        }
        return packageType;
    }

    public List<PackageType> findAll() {
        return entityManager.createNamedQuery("getAllPackageTypes", PackageType.class).getResultList();
    }

    public PackageType findWithMandatorySensors(Long id) throws MyEntityNotFoundException {
        var packageType = this.find(id);

        Hibernate.initialize(packageType.getMandatorySensors());
        return packageType;
    }

    public List<PackageType> findAllWithMandatorySensors() {
        var packageTypes = this.findAll();
        packageTypes.forEach(packageType -> Hibernate.initialize(packageType.getMandatorySensors()));
        return packageTypes;
    }

    public PackageType create(String name) throws MyEntityExistsException {
        if(!entityManager.createNamedQuery("getPackageTypeByName", PackageType.class)
                .setParameter("name", name)
                .getResultList().isEmpty()) {
            throw new MyEntityExistsException("PackageType (" + name + ") already exists");
        }
        var packageType = new PackageType(name);
        entityManager.persist(packageType);
        return packageType;
    }

    public PackageType update(Long id, String name) throws MyEntityNotFoundException, MyEntityExistsException {
        var packageType = this.find(id);
        //if a package type with the same name already exists and its a different package type from the one passed with the id  then throw exception
        if(!entityManager.createNamedQuery("getPackageTypeByName", PackageType.class)
                .setParameter("name", name)
                .getResultList().isEmpty() && !packageType.getName().equals(name)) {
            throw new MyEntityExistsException("PackageType (" + name + ") already exists");
        }
        packageType.setName(name);
        return packageType;
    }

    public void addMandatorySensor(Long id, Long sensorTypeId) throws MyEntityNotFoundException, MyEntityExistsException {
        var packageType = this.find(id);
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if(sensorType == null) {
            throw new MyEntityNotFoundException("Sensor (" + sensorTypeId + ") not found");
        }
        if(packageType.getMandatorySensors().contains(sensorType)) {
            throw new MyEntityExistsException("Sensor (" + sensorTypeId + ") already exists in PackageType (" + id + ")");
        }
        packageType.addMandatorySensor(sensorType);
        if(sensorType.getPackageTypes().contains(packageType)) {
            throw new MyEntityExistsException("PackageType (" + id + ") already exists in Sensor (" + sensorTypeId + ")");
        }
        sensorType.addPackageType(packageType);
    }

    public void removeMandatorySensor(Long id, Long sensorTypeId) throws MyEntityNotFoundException {
        var packageType = this.find(id);
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if(sensorType == null) {
            throw new MyEntityNotFoundException("Sensor (" + sensorTypeId + ") not found");
        }
        if(!packageType.getMandatorySensors().contains(sensorType)) {
            throw new MyEntityNotFoundException("Sensor (" + sensorTypeId + ") not found in PackageType (" + id + ")");
        }
        packageType.removeMandatorySensor(sensorType);
        if(!sensorType.getPackageTypes().contains(packageType)) {
            throw new MyEntityNotFoundException("PackageType (" + id + ") not found in Sensor (" + sensorTypeId + ")");
        }
        sensorType.removePackageType(packageType);
    }
}
