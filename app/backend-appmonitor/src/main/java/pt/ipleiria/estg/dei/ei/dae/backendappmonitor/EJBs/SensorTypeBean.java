package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public SensorType find(Long id) throws MyEntityNotFoundException {
        var sensor = entityManager.find(SensorType.class, id);
        if(sensor == null) {
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

    public SensorType create(String name, String unit, double ceiling, double floor) {
        var sensorType = new SensorType(name, unit, ceiling, floor);
        entityManager.persist(sensorType);
        return sensorType;
    }

    public SensorType update(Long id, String name, String unit, double ceiling, double floor) throws MyEntityNotFoundException {
        var sensorType = this.find(id);
        entityManager.lock(sensorType, LockModeType.OPTIMISTIC);
        sensorType.setName(name);
        sensorType.setUnit(unit);
        sensorType.setCeiling(ceiling);
        sensorType.setFloor(floor);
        return sensorType;
    }


}
