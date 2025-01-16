package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;

import java.util.List;

@Stateless
public class SensorTypeBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private XLSXFileBean xlsxFileBean;

    public SensorType find(Long id) throws MyEntityNotFoundException {
        var sensor = entityManager.find(SensorType.class, id);
        if (sensor == null) {
            throw new MyEntityNotFoundException("SensorType with id: '" + id + "' not found");
        }
        return sensor;
    }

    public SensorType findByName(String name) {
        return entityManager.createNamedQuery("getSensorTypeByName", SensorType.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public List<SensorType> findAll() {
        return entityManager.createNamedQuery("getAllSensorTypes", SensorType.class).getResultList();
    }

    public SensorType create(long id, String name, String unit, double ceiling, double floor) throws MyEntityExistsException, MyIllegalArgumentException {
        if (entityManager.find(SensorType.class, id) != null) {
            throw new MyEntityExistsException("ProductType with id: '" + id + "' already exists");
        }

        if(id<0) throw new MyIllegalArgumentException("Id must be greater than 0");
        if(name == null || name.isEmpty()) throw new MyIllegalArgumentException("Name must be defined");
        if(unit == null || unit.isEmpty()) throw new MyIllegalArgumentException("Unit must be defined");
        if(ceiling<floor) throw new MyIllegalArgumentException("Ceiling must be greater than floor");
        var sensorType = new SensorType(id, name, unit, ceiling, floor);
        entityManager.persist(sensorType);
        xlsxFileBean.saveAllSensorTypesToXlsx();
        return sensorType;
    }

    public SensorType update(Long id, String name, String unit, double ceiling, double floor) throws MyEntityNotFoundException, MyIllegalArgumentException {
        var sensorType = this.find(id);
        entityManager.lock(sensorType, LockModeType.OPTIMISTIC);

        if(name != null && !name.isEmpty()) sensorType.setName(name);
        if(unit != null && !unit.isEmpty()) sensorType.setUnit(unit);
        if(ceiling<floor) throw new MyIllegalArgumentException("Ceiling must be greater than floor");
        sensorType.setCeiling(ceiling);
        sensorType.setFloor(floor);
        xlsxFileBean.saveAllSensorTypesToXlsx();
        return sensorType;
    }


}
