package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.io.*;
import java.util.Date;
import java.util.List;

@Stateless
public class SensorBean implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private EmailBean emailBean;


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

    public List<Sensor> findAllWithHistory() {
        var sensors = this.findAll();
        sensors.forEach(sensor -> Hibernate.initialize(sensor.getHistory()));
        return sensors;
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
        //entityManager.lock(sensor, LockModeType.OPTIMISTIC);
        sensor.setSensorType(sensorType);
        sensor.setVolume(volume);
        return sensor;
    }

    public void addValue(Long sensorId, SensorRecordDTO sensorRecordDTO) throws MyEntityNotFoundException {
        var sensor = this.find(sensorId);
        var date = new Date();
        var value = sensorRecordDTO.getValue();
        SensorRecord record = new SensorRecord(date,value,sensor);
        entityManager.persist(record);
        sensor.addRecord(record);

        //send email for all managers and customer of that order
        var managers = entityManager.createNamedQuery("getAllManagers", Manager.class).getResultList();
        for (var manager : managers) {
            emailBean.send(manager.getEmail(), "New value for sensor with id: '" + sensorId + "'",
                    "The sensor with id: '" + sensorId + "' has a new value: '" + value + "' at: '" + date.toString() + "'");
        }
        var customer = sensor.getVolume().getOrder().getCustomer();
        emailBean.send(customer.getEmail(), "New value for sensor with id: '" + sensorId + "'",
                "The sensor with id: '" + sensorId + "' has a new value: '" + value + "' at: '" + date.toString() + "'");
    }

}
