package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyIllegalArgumentException;

import java.io.*;
import java.util.Date;
import java.util.List;

@Stateless
public class SensorBean implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private EmailBean emailBean;
    @EJB
    private SensorRecordBean sensorRecordBean;


    public Sensor find(Long id) throws MyEntityNotFoundException {
        var sensor = entityManager.find(Sensor.class, id);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor with id: '" + id + "' not found");
        }
        return sensor;
    }

    public List<Sensor> findAll() {
        var sensors = entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
        if(sensors.isEmpty()){
            throw new MyEntityNotFoundException("No sensors found");
        }
        return sensors;
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

    public Sensor create(Long id, Long sensorTypeId, Long volumeId) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if(sensorTypeId == null || sensorTypeId <= 0) throw new MyIllegalArgumentException("SensorType id must be greater than 0");
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if (sensorType == null) {
            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found");
        }
        if(volumeId == null || volumeId <= 0) throw new MyIllegalArgumentException("Volume id must be greater than 0");
        var volume = entityManager.find(Volume.class, volumeId);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume with id: '" + volumeId + "' not found");
        }
        if(id == null || id <= 0) throw new MyIllegalArgumentException("Sensor id must be greater than 0");
        var sensor = new Sensor(id, sensorType, volume);
        sensorType.addSensor(sensor);
        volume.addSensor(sensor);
        entityManager.persist(sensor);
        return sensor;
    }

    //NAO FAZ SENTIDO POIS TERIA DE SE DEIXAR UM VOLUME COM UM SENSOR OBRIGATORIO A MENOS
//    public Sensor update(Long id, Long sensorTypeId, Long volumeId) throws MyEntityNotFoundException, MyIllegalArgumentException {
//        if(sensorTypeId == null || sensorTypeId <= 0) throw new MyIllegalArgumentException("SensorType id must be greater than 0");
//        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
//        if(sensorType == null) {
//            throw new MyEntityNotFoundException("SensorType with id: '" + sensorTypeId + "' not found");
//        }
//        if(volumeId == null || volumeId <= 0) throw new MyIllegalArgumentException("Volume id must be greater than 0");
//        var volume = entityManager.find(Volume.class, volumeId);
//        if (volume == null) {
//            throw new MyEntityNotFoundException("Volume with id: '" + volumeId + "' not found");
//        }
//        if(id == null || id <= 0) throw new MyIllegalArgumentException("Sensor id must be greater than 0");
//        var sensor = this.find(id);
//        entityManager.lock(sensor, LockModeType.OPTIMISTIC);
//        sensor.getVolume().removeSensor(sensor);
//        sensor.setVolume(volume);
//        volume.addSensor(sensor);
//
//        sensor.getSensorType().removeSensor(sensor);
//        sensor.setSensorType(sensorType);
//        sensorType.addSensor(sensor);
//        return sensor;
//    }

    public void addValue(Long sensorId, SensorRecordDTO sensorRecordDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if (sensorId == null || sensorId <= 0) throw new MyIllegalArgumentException("Sensor id must be greater than 0");

        var sensor = this.find(sensorId);
        var date = new Date();
        var value = sensorRecordDTO.getValue();
        entityManager.lock(sensor, LockModeType.OPTIMISTIC);
        sensorRecordBean.create(date, value, sensor.getId());

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

    public void removeValue(Long sensorId, Long recordId) throws MyEntityNotFoundException, MyIllegalArgumentException {
        if (sensorId == null || sensorId <= 0) throw new MyIllegalArgumentException("Sensor id must be greater than 0");
        var sensor = this.find(sensorId);
        sensorRecordBean.delete(recordId);
    }

}
