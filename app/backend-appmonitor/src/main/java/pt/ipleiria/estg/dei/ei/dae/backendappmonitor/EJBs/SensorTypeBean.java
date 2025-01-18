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

    public SensorType find(Long id) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("SensorType id must be greater than 0");
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
        var sensorTypes = entityManager.createNamedQuery("getAllSensorTypes", SensorType.class).getResultList();
        if(sensorTypes.isEmpty()){
            throw new MyEntityNotFoundException("No SensorTypes found");
        }
        return sensorTypes;
    }

    public SensorType create(Long id, String name, String unit, Double ceiling, Double floor) throws MyEntityExistsException, MyIllegalArgumentException {
        if(id == null || id<=0) throw new MyIllegalArgumentException("Id must be greater than 0");
        if (entityManager.find(SensorType.class, id) != null) {
            throw new MyEntityExistsException("SensorType with id: '" + id + "' already exists");
        }
        if(!entityManager.createNamedQuery("getSensorTypeByName", SensorType.class)
                .setParameter("name", name)
                .getResultList().isEmpty()){
            throw new MyEntityExistsException("SensorType with name: '" + name + "' already exists");
        }
        if(name == null || name.isEmpty()) throw new MyIllegalArgumentException("Name must be defined");
        if(unit == null || unit.isEmpty()) throw new MyIllegalArgumentException("Unit must be defined");
        if(ceiling == null || floor == null) throw new MyIllegalArgumentException("Ceiling and floor must be defined");
        if(ceiling<floor) throw new MyIllegalArgumentException("Ceiling must be greater than floor");
        var sensorType = new SensorType(id, name, unit, ceiling, floor);
        entityManager.persist(sensorType);
        xlsxFileBean.saveAllSensorTypesToXlsx();
        return sensorType;
    }

    public SensorType update(Long id, String name, String unit, Double ceiling, Double floor) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if(id == null || id<=0) throw new MyIllegalArgumentException("Id must be greater than 0");
        var sensorType = this.find(id);
        entityManager.lock(sensorType, LockModeType.OPTIMISTIC);

        if(name != null && !name.isEmpty()) sensorType.setName(name);
        if(unit != null && !unit.isEmpty()) sensorType.setUnit(unit);
        if(ceiling != null && ceiling>sensorType.getFloor()) sensorType.setCeiling(ceiling);
        if(floor != null && floor<sensorType.getCeiling()) sensorType.setFloor(floor);
        xlsxFileBean.saveAllSensorTypesToXlsx();
        return sensorType;
    }


}
