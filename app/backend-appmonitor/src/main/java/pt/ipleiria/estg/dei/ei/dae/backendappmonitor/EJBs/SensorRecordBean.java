package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;


import jakarta.ejb.Stateless;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.Date;


@Stateless
public class SensorRecordBean {
    @PersistenceContext
    private EntityManager entityManager;

    public SensorRecord create(Date date, double value,long sensorId) {
        var sensor = entityManager.find(Sensor.class, sensorId);
        if(sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + sensorId + "' not found");
        }
        var sensorRecord = new SensorRecord(date, value, sensor);
        entityManager.persist(sensorRecord);
        sensor.addRecord(sensorRecord);
        return sensorRecord;
    }

    public SensorRecord update(long id, Date date, double value) {
        var sensorRecord = find(id);
        entityManager.lock(sensorRecord, LockModeType.OPTIMISTIC);
        sensorRecord.setTime(date);
        sensorRecord.setValue(value);
        return sensorRecord;
    }

    public SensorRecord delete(long id) {
        var sensorRecord = find(id);
        sensorRecord.getSensor().removeRecord(sensorRecord);
        entityManager.remove(sensorRecord);
        return sensorRecord;
    }

    public SensorRecord find(long id) {
        var sensorRecord = entityManager.find(SensorRecord.class, id);
        if(sensorRecord == null) {
            throw new MyEntityNotFoundException("SensorRecord with id: '" + id + "' not found");
        }
        return sensorRecord;
    }


}
