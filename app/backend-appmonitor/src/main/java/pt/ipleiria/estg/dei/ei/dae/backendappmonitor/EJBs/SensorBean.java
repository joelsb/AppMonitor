package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
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

    public Sensor create(long sensorTypeId, long volumeId) {


            var sensorType = entityManager.find(SensorType.class, sensorTypeId);
            var volume = entityManager.find(Volume.class, volumeId);
            var sensor = new Sensor(sensorType, volume);
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
