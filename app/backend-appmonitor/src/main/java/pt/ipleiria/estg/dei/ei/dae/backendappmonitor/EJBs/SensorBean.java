package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;
import java.io.*;
import java.util.List;

@Stateless
public class SensorBean implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;


    public Sensor find(Long id) {
        return entityManager.find(Sensor.class, id);
    }

    public List<Sensor> findAll() {
        return entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
    }
    public Sensor findWithHistory(Long id) {
        var sensor = this.find(id);
        //Initialize the lazy collection
        Hibernate.initialize(sensor.getHistory());
        return sensor;
    }

    public Sensor findWithHistoryAndSensorType(Long id) {
        var sensor = this.find(id);
        //Initialize the lazy collection
        Hibernate.initialize(sensor.getHistory());
        Hibernate.initialize(sensor.getSensorType());
        return sensor;
    }

    public Sensor create(long sensorTypeId, Long volumeId) {


            var sensorType = entityManager.find(SensorType.class, sensorTypeId);
            //var volume = entityManager.find(Volume.class, volumeId);
            var sensor = new Sensor(sensorType, null);
            entityManager.persist(sensor);
            return sensor;
    }

    public Sensor update(long id, long sensorTypeId, long volumeId) {
        var sensorType = entityManager.find(SensorType.class, sensorTypeId);
        var volume = entityManager.find(Volume.class, volumeId);
        var sensor = entityManager.find(Sensor.class, id);
        sensor.setSensorType(sensorType);
        sensor.setVolume(volume);
        return sensor;
    }
}
