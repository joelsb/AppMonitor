package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.OrderCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.ProductRecordDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.VolumeCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class OrderBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private VolumeBean volumeBean;

    public Order find(long id) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if (order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        return order;
    }

    public List<Order> findAll() {
        return entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
    }

    public Order create(OrderCreateDTO orderCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        // Check if the order already exists
        var existingOrder = entityManager.find(Order.class, orderCreateDTO.getId());
        if (existingOrder != null) {
            throw new MyEntityExistsException("Order (" + orderCreateDTO.getId() + ") already exists");
        }

        var orderId = orderCreateDTO.getId();
        var customerUsername = orderCreateDTO.getCustomerUsername();
        var createdDate = orderCreateDTO.getCreatedDate();
        var volumeDTO = orderCreateDTO.getVolume();


        //TODO: FALTA SEPARAR A CRIAÇAÔ DOS OBJECTOS DA VALIDAÇAO

        // Find the customer
        var customer = entityManager.find(Customer.class, customerUsername);
        if (customer == null) {
            throw new MyEntityNotFoundException("Customer with username " + customerUsername + " not found");
        }

        // Create the order (without persisting)
        var order = new Order(orderId, createdDate, null, customer);
        customer.addOrder(order);

        var packageTypeId = volumeDTO.getPackageTypeId();
        var packageType = entityManager.find(PackageType.class, packageTypeId);
        if (packageType == null) {
            throw new MyEntityNotFoundException("PackageType with id " + packageTypeId + " not found");
        }

        // Create the volume
        if (entityManager.find(Volume.class, volumeDTO.getId()) != null) {
            throw new MyEntityExistsException("Volume with id " + volumeDTO.getId() + " already exists");
        }
        var volume = new Volume(volumeDTO.getId(), volumeDTO.getSentDate(), packageType, order);
        packageType.addVolume(volume);
        order.addVolume(volume);

        // Create sensors
        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
            var sensorType = entityManager.find(SensorType.class, sensorDTO.getSensorTypeId());
            if (sensorType == null) {
                throw new MyEntityNotFoundException("SensorType not found for id: " + sensorDTO.getSensorTypeId());
            }
            if(entityManager.find(Sensor.class, sensorDTO.getId()) != null){
                throw new MyEntityExistsException("Sensor with id " + sensorDTO.getId() + " already exists");
            }
            var sensor = new Sensor(sensorDTO.getId(), sensorType, volume);
            volume.addSensor(sensor);
        }

        // Create product records
        for (ProductRecordDTO productDTO : volumeDTO.getProducts()) {
            var product = entityManager.find(ProductType.class, productDTO.getProductId());
            if (product == null) {
                throw new MyEntityNotFoundException("ProductType not found for id: " + productDTO.getProductId());
            }
            var productRecord = new ProductRecord(product, productDTO.getQuantity(), volume);
            volume.addProduct(productRecord);
        }
        //persist everything
        entityManager.persist(order);

        return order;
    }


    public Order updateDeliveredDate(long id, Date deliveredDate) throws MyEntityNotFoundException {
        var order = this.find(id);
        order.setDeliveredDate(deliveredDate);
        return order;
    }

    public Order addVolumes(long id, List<Long> volumesIds) throws MyEntityNotFoundException {
        var order = this.find(id);
        for (long volumeId : volumesIds) {
            var volume = entityManager.find(Volume.class, volumeId);
            if (volume == null) {
                throw new MyEntityNotFoundException("Volume (" + volumeId + ") not found");
            } else {
                if (order.getVolumes().contains(volume)) {
                    throw new MyEntityNotFoundException("Volume (" + volumeId + ") already in order (" + id + ")");
                }
                order.addVolume(volume);
                volume.setOrder(order);
            }
        }
        return order;
    }


    public Order findWithVolumes(long id) throws MyEntityNotFoundException {
        var order = this.find(id);
        Hibernate.initialize(order.getVolumes());
        return order;
    }

    public Order findWithVolumesProductsSensors(long id) throws MyEntityNotFoundException {
        var order = this.findWithVolumes(id);
        for (Volume volume : order.getVolumes()) {
            Hibernate.initialize(volume.getProducts());
            Hibernate.initialize(volume.getSensors());
        }
        return order;
    }

    public List<Order> findAvailableOrders() {
        List<Order> orders = null;
        for (Order order : findAll()) {
            if (order.getDeliveredDate() == null) {
                orders.add(order);
            }
        }
        return orders;
    }
}
