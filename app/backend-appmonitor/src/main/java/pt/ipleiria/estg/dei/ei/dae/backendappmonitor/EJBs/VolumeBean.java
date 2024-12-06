package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

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

    public Volume addVolumeToOrder(VolumeCreateDTO volumeCreateDTO) throws MyEntityNotFoundException {
        var orderId = volumeCreateDTO.getOrderId();
        var order = entityManager.find(Order.class, orderId);
        if(order == null) {
            throw new MyEntityNotFoundException("Order with id " + orderId + " not found");
        }
        var volume = create(volumeCreateDTO, order);
        order.addVolume(volume);
        return volume;
    }


    public Volume create(VolumeCreateDTO volumeCreateDTO, Order order) throws MyEntityNotFoundException {
        /*
        long id, Date sentDate, PackageType packageType, Order order
         */
        var packageTypeId = volumeCreateDTO.getPackageTypeId();
        var packageType = entityManager.find(PackageType.class, packageTypeId);
        if(packageType == null) {
            throw new MyEntityNotFoundException("PackageType with id " + packageTypeId + " not found");
        }

        //Create the Volume
        var volume = new Volume(volumeCreateDTO.getId(), volumeCreateDTO.getSentDate(), packageType, order);
        packageType.addVolume(volume);

        //Create the Product Records
        for (ProductRecordDTO productDTO : volumeCreateDTO.getProducts()) {
            var product = entityManager.find(ProductType.class, productDTO.getProductId());
            if (product == null) {
                throw new MyEntityNotFoundException("ProductType not found for id: " + productDTO.getProductId());
            }
            var productRecord = new ProductRecord(product, productDTO.getQuantity(), volume);
            //Product_id em ProductRecord esta a null

            volume.addProduct(productRecord);
        }
        //Create the sensors
        for (SensorDTO sensorDTO : volumeCreateDTO.getSensors()) {
            var sensorType = entityManager.find(SensorType.class, sensorDTO.getSensorTypeId());
            if (sensorType == null) {
                throw new MyEntityNotFoundException("SensorType not found for id: " + sensorDTO.getSensorTypeId());
            }
            var sensor = new Sensor(sensorDTO.getId(), sensorType, volume);
            volume.addSensor(sensor);
        }

        //persist everything
        entityManager.persist(volume);
        return volume;
    }

}
