package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.PackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;

import java.util.List;


@Stateless
public class PackageTypeBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private XLSXFileBean xlsxFileBean;

    public PackageType find(long id) throws MyEntityNotFoundException {
        var packageType = entityManager.find(PackageType.class, id);
        if(packageType == null) {
            throw new MyEntityNotFoundException("PackageType with id: '" + id + "' not found");
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

    public PackageType create(Long id, String name) throws MyEntityExistsException, MyIllegalArgumentException {
        if(!entityManager.createNamedQuery("getPackageTypeByName", PackageType.class)
                .setParameter("name", name)
                .getResultList().isEmpty()) {
            throw new MyEntityExistsException("PackageType with name: '" + name + "' already exists");
        }
        if(id ==null || id<=0) throw new MyIllegalArgumentException("PackageType id must be greater than 0");
        if(entityManager.find(PackageType.class, id) != null){
            throw new MyEntityExistsException("PackageType with id: '" + id + "' already exists");
        }
        if(name == null || name.isEmpty()) throw new MyIllegalArgumentException("PackageType name cannot be empty");
        var packageType = new PackageType(id, name);
        entityManager.persist(packageType);
        xlsxFileBean.saveAllPackageTypesToXlsx();
        return packageType;
    }

    public PackageType update(Long id, String name) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("PackageType id must be greater than 0");
        var packageType = this.find(id);
        //if a package type with the same name already exists and its a different package type from the one passed with the id  then throw exception
        if(!entityManager.createNamedQuery("getPackageTypeByName", PackageType.class)
                .setParameter("name", name)
                .getResultList().isEmpty() && !packageType.getName().equals(name)) {
            throw new MyEntityExistsException("PackageType with name: '" + name + "' already exists");
        }
        entityManager.lock(packageType, LockModeType.OPTIMISTIC);
        if(name != null) packageType.setName(name);
        xlsxFileBean.saveAllPackageTypesToXlsx();
        return packageType;
    }

    public void addMandatorySensor(Long id, Long sensorTypeId) throws MyEntityNotFoundException, MyEntityExistsException, MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("PackageType id must be greater than 0");
        var packageType = this.find(id);
        if(sensorTypeId == null || sensorTypeId <= 0) throw new MyIllegalArgumentException("SensorType id must be greater than 0");
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if(sensorType == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + sensorTypeId + "' not found");
        }
        if(packageType.getMandatorySensors().contains(sensorType)) {
            throw new MyEntityExistsException("Sensor with id: '" + sensorTypeId + "' already exists in PackageType with id: '" + id + "'");
        }
        packageType.addMandatorySensor(sensorType);
        if(sensorType.getPackageTypes().contains(packageType)) {
            throw new MyEntityExistsException("PackageType with id: '" + id + "' already exists in Sensor with id: '" + sensorTypeId + "'");
        }
        sensorType.addPackageType(packageType);
    }

    public void removeMandatorySensor(Long id, Long sensorTypeId) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("PackageType id must be greater than 0");
        var packageType = this.find(id);
        if(sensorTypeId == null || sensorTypeId <= 0) throw new MyIllegalArgumentException("SensorType id must be greater than 0");
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if(sensorType == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + sensorTypeId + "' not found");
        }
        if(!packageType.getMandatorySensors().contains(sensorType)) {
            throw new MyEntityNotFoundException("Sensor with id: '" + sensorTypeId + "' not found in PackageType with id: '" + id + "'");
        }
        packageType.removeMandatorySensor(sensorType);
        if(!sensorType.getPackageTypes().contains(packageType)) {
            throw new MyEntityNotFoundException("PackageType with id: '" + id + "' not found in Sensor with id: '" + sensorTypeId + "'");
        }
        sensorType.removePackageType(packageType);
    }
}
