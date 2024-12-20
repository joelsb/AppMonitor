package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;
import java.util.Date;
import java.util.List;

@Stateless
public class VolumeBean {

    @PersistenceContext
    private EntityManager entityManager;

    public Volume create(Date sentDate , PackageType pack, List<ProductRecord> products,List<Sensor> sensors, Order order) {
        var volume = new Volume(sentDate ,pack,  products, sensors, order);
        entityManager.persist(volume);
        return volume;
    }
    public Volume addSensor(long id, Sensor sensor) throws MyEntityNotFoundException {
        var volume = entityManager.find(Volume.class, id);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume (" + id + ") not found");
        }
        volume.addSensor(sensor);
        sensor.setVolume(volume);
        return volume;
    }
    public Volume addProduct(long id, ProductRecord productRecord) throws MyEntityNotFoundException {
        var volume = entityManager.find(Volume.class, id);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume (" + id + ") not found");
        }
        volume.addProduct(productRecord);
        productRecord.setVolume(volume);
        return volume;
    }
}
