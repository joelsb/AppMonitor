package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;


import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;

import java.util.Date;


@Stateless
public class SensorRecordBean {
    @PersistenceContext
    private EntityManager entityManager;

    public SensorRecord create(Date date, double value,Sensor sensor) {
        var sensorRecord = new SensorRecord(date, value,sensor);
        entityManager.persist(sensorRecord);
        return sensorRecord;
    }

}
