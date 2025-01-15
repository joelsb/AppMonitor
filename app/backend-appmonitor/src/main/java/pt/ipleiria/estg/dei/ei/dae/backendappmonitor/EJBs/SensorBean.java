package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.io.*;
import java.util.Date;
import java.util.List;

@Stateless
public class SensorBean implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;


    public Sensor find(Long id) throws MyEntityNotFoundException {
        var sensor = entityManager.find(Sensor.class, id);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + id + "' not found");
        }
        return sensor;
    }

    public List<Sensor> findAll() {
        return entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
    }

    public Sensor findWithHistory(Long id) throws MyEntityNotFoundException {
        var sensor = this.find(id);
        //Initialize the lazy collection
        Hibernate.initialize(sensor.getHistory());
        return sensor;
    }

    public Sensor create(long id, long sensorTypeId, Long volumeId) throws MyEntityNotFoundException {
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if (sensorType == null) {
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found");
        }
        var volume = entityManager.find(Volume.class, volumeId);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume with id: '" + volumeId + "' not found");
        }
        var sensor = new Sensor(id, sensorType, volume);
        entityManager.persist(sensor);
        return sensor;
    }

    public Sensor update(long id, long sensorTypeId, long volumeId) throws MyEntityNotFoundException {
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if(sensorType == null) {
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found");
        }
        var volume = entityManager.find(Volume.class, volumeId);
        if (volume == null) {
            throw new MyEntityNotFoundException("Volume with id: '" + volumeId + "' not found");
        }
        var sensor = entityManager.find(Sensor.class, id);
        if(sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + id + "' not found");
        }
        sensor.setSensorType(sensorType);
        sensor.setVolume(volume);
        return sensor;
    }

    public void addValue(Long sensorId, SensorRecordDTO sensorRecordDTO) throws MyEntityNotFoundException {
        var sensor = this.find(sensorId);
        var date = new Date();
        //TODO: Send the notification the the ones that are subscribed to this sensor
        var value = sensorRecordDTO.getValue();
        SensorRecord record = new SensorRecord(date,value, sensor);
        sensor.addRecord(record);
    }

}
