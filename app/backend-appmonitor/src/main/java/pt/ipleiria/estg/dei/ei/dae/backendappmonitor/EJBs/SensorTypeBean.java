package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;

import java.util.List;

@Stateless
public class SensorTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public SensorType find(Long id) {
        return entityManager.find(SensorType.class, id);
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

    public SensorType update(Long id, String name, String unit, double ceiling, double floor) {
        var sensorType = entityManager.find(SensorType.class, id);
        sensorType.setName(name);
        sensorType.setUnit(unit);
        sensorType.setCeiling(ceiling);
        sensorType.setFloor(floor);
        return sensorType;
    }


}
