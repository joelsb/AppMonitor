package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

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

    public SensorType create(long id, String name, String unit, double ceiling, double floor) throws MyEntityExistsException {
        if (entityManager.find(SensorType.class, id) != null) {
            throw new MyEntityExistsException("ProductType with id: '" + id + "' already exists");
        }
        var sensorType = new SensorType(id, name, unit, ceiling, floor);
        entityManager.persist(sensorType);
        xlsxFileBean.saveAllSensorTypesToXlsx();
        return sensorType;
    }

    public SensorType update(Long id, String name, String unit, double ceiling, double floor) throws MyEntityNotFoundException {
        var sensorType = this.find(id);
        sensorType.setName(name);
        sensorType.setUnit(unit);
        sensorType.setCeiling(ceiling);
        sensorType.setFloor(floor);
        xlsxFileBean.saveAllSensorTypesToXlsx();
        return sensorType;
    }


}
