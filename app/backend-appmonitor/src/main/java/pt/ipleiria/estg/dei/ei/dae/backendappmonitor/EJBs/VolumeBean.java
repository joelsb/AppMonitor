package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;


import java.util.Date;

import java.util.List;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Volume find(long id) throws MyEntityNotFoundException {
        var volume = entityManager.find(Volume.class, id);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume with id " + id + " not found");
        }
        return volume;
    }

    public List<Volume> findAll() {
        return entityManager.createNamedQuery("getAllVolumes", Volume.class).getResultList();
    }

    public Volume findWithSensorsProducts(long id) throws MyEntityNotFoundException {
        var volume = entityManager.find(Volume.class, id);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume with id " + id + " not found");
        }
        Hibernate.initialize(volume.getSensors());
        Hibernate.initialize(volume.getProducts());
        return volume;
    }

    public List<Volume> findAllWithSensorsProducts() {
        var volumes = entityManager.createNamedQuery("getAllVolumes", Volume.class).getResultList();
        volumes.forEach(volume -> {
            Hibernate.initialize(volume.getSensors());
            Hibernate.initialize(volume.getProducts());
        });
        return volumes;
    }

    public Volume addVolumeToOrder(VolumeCreateDTO volumeCreateDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        var orderId = volumeCreateDTO.getOrderId();
        var order = entityManager.find(Order.class, orderId);
        if(order == null) {
            throw new MyEntityNotFoundException("Order with id " + orderId + " not found");
        }
        var volume = this.create(volumeCreateDTO, order);
        order.addVolume(volume);
        entityManager.persist(volume);
        return volume;
    }

    public Volume setDelivered(long id) throws MyEntityNotFoundException {
        var volume = entityManager.find(Volume.class, id);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume with id " + id + " not found");
        }
        //data de hoje
        Date date = Date.from(new Date().toInstant());

        volume.setDeliveredDate(date);
        entityManager.persist(volume);
        return volume;
    }


    public Volume create(VolumeCreateDTO volumeCreateDTO, Order order) throws MyEntityNotFoundException, MyEntityExistsException {
        /*
        long id, Date sentDate, PackageType packageType, Order order
         */
        // Find the package type
        //Validate everything first
        var packageTypeId = volumeCreateDTO.getPackageTypeId();
        var packageType = entityManager.find(PackageType.class, packageTypeId);
        if (packageType == null) {
            throw new MyEntityNotFoundException("PackageType with id " + packageTypeId + " not found");
        }

        // Create the volume
        if (entityManager.find(Volume.class, volumeCreateDTO.getId()) != null) {
            throw new MyEntityExistsException("Volume with id " + volumeCreateDTO.getId() + " already exists");
        }

        // Validate sensors
        for (SensorDTO sensorDTO : volumeCreateDTO.getSensors()) {
            var sensorType = entityManager.find(SensorType.class, sensorDTO.getSensorTypeId());
            if (sensorType == null) {
                throw new MyEntityNotFoundException("SensorType not found for id: " + sensorDTO.getSensorTypeId());
            }
            if(entityManager.find(Sensor.class, sensorDTO.getId()) != null){
                throw new MyEntityExistsException("Sensor with id " + sensorDTO.getId() + " already exists");
            }
        }

        // Validate product records
        for (ProductRecordDTO productDTO : volumeCreateDTO.getProducts()) {
            var product = entityManager.find(ProductType.class, productDTO.getProductId());
            if (product == null) {
                throw new MyEntityNotFoundException("ProductType not found for id: " + productDTO.getProductId());
            }
        }

        //create everything else
        var volume = new Volume(volumeCreateDTO.getId(), volumeCreateDTO.getSentDate(), packageType, order);
        packageType.addVolume(volume);
        order.addVolume(volume);

        // Create sensors
        for (SensorDTO sensorDTO : volumeCreateDTO.getSensors()) {
            var sensorType = entityManager.find(SensorType.class, sensorDTO.getSensorTypeId());
            var sensor = new Sensor(sensorDTO.getId(), sensorType, volume);
            entityManager.persist(sensor);
            volume.addSensor(sensor);
        }

        // Create product records
        for (ProductRecordDTO productDTO : volumeCreateDTO.getProducts()) {
            var product = entityManager.find(ProductType.class, productDTO.getProductId());
            var productRecord = new ProductRecord(product, productDTO.getQuantity(), volume);
            entityManager.persist(productRecord);
            volume.addProduct(productRecord);
        }



        //persist everything else
        entityManager.persist(volume);

        return volume;
    }

}
