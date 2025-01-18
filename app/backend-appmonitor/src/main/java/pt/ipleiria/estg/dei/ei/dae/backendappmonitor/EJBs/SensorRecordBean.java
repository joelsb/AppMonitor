package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;


import jakarta.ejb.Stateless;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;

import java.util.Date;


@Stateless
public class SensorRecordBean {
    @PersistenceContext
    private EntityManager entityManager;

    public SensorRecord create(Date date, Double value,Long sensorId) throws MyIllegalArgumentException {
        if(sensorId == null || sensorId <= 0) throw new MyIllegalArgumentException("Sensor id must be greater than 0");
        var sensor = entityManager.find(Sensor.class, sensorId);
        if(sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + sensorId + "' not found");
        }
        if(date == null) throw new MyIllegalArgumentException("Date must not be null");
        if(value == null) throw new MyIllegalArgumentException("Value must not be null");
        var sensorRecord = new SensorRecord(date, value, sensor);
        entityManager.persist(sensorRecord);
        sensor.addRecord(sensorRecord);
        return sensorRecord;
    }

    public SensorRecord update(Long id, Date date, Double value) throws MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("SensorRecord id must be greater than 0");
        var sensorRecord = find(id);
        entityManager.lock(sensorRecord, LockModeType.OPTIMISTIC);
        if(date == null) throw new MyIllegalArgumentException("Date must not be null");
        sensorRecord.setTime(date);
        if(value == null) throw new MyIllegalArgumentException("Value must not be null");
        sensorRecord.setValue(value);
        return sensorRecord;
    }

    public void delete(Long id) throws MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("SensorRecord id must be greater than 0");
        var sensorRecord = find(id);
        sensorRecord.getSensor().removeRecord(sensorRecord);
        entityManager.remove(sensorRecord);
    }

    public SensorRecord find(Long id) throws MyIllegalArgumentException {
        if(id == null || id <= 0) throw new MyIllegalArgumentException("SensorRecord id must be greater than 0");
        var sensorRecord = entityManager.find(SensorRecord.class, id);
        if(sensorRecord == null) {
            throw new MyEntityNotFoundException("SensorRecord with id: '" + id + "' not found");
        }
        return sensorRecord;
    }


}
