package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.User;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;
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

    public Order create(Date createdDate, String customerUsername) throws MyEntityExistsException, MyEntityNotFoundException {


            var customer = entityManager.find(Customer.class, customerUsername);
            var order = new Order(createdDate,customer);
            customer.addOrder(order);
            //TODO: CREATE VOLUMES AND ASSOCIATE THEM TO THE ORDER
            entityManager.persist(order);
            return order;
    }


    public Order addVolume(long id, Volume volume) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if(order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        else{
            order.addVolume(volume);
            volume.setOrder(order);
        }
        return order;
    }

    public Order find(long id) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if (order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        return order;
    }

    public List<Order> findAvailableOrders() {
        var orders = entityManager.createNamedQuery("getAvailableOrders", Order.class).getResultList();
        for (Order order : orders) {
            Hibernate.initialize(order.getVolumes());
        }
        return orders;
    }

    public List<Volume> findVolumes(long id) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if (order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        return entityManager.createNamedQuery("getVolumesByOrder", Volume.class)
                .setParameter("order", order)
                .getResultList();
    }

    public List<Order> findAll() {
        var orders = entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
        for (Order order : orders) {
            Hibernate.initialize(order.getVolumes());
        }
        return orders;
    }

    public Order create(OrderCreateDTO orderCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        //TODO: Falta utilizar os beans aqui tbm, reutilizar codigo

        // Validate input and dependencies

        validateOrderCreation(orderCreateDTO);

        // Create entities
        var customer = entityManager.find(Customer.class, orderCreateDTO.getCustomerUsername());
        var order = new Order(orderCreateDTO.getCreatedDate(), customer);
        customer.addOrder(order);

        entityManager.persist(order);

        var volumeDTO = orderCreateDTO.getVolume();
        var packageType = entityManager.find(PackageType.class, volumeDTO.getPackageTypeName());
        var volume = new Volume(volumeDTO.getSentDate(), packageType, order);
        packageType.addVolume(volume);
        order.addVolume(volume);

        entityManager.persist(volume);

        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
            var sensorType = entityManager.find(SensorType.class, sensorDTO.getSensorTypeId());
            var sensor = new Sensor(sensorType, volume);
            volume.addSensor(sensor);

            // Persist each sensor
            entityManager.persist(sensor);
        }

        for (ProductRecordDTO productDTO : volumeDTO.getProducts()) {
            var product = entityManager.find(ProductType.class, productDTO.getProductId());
            var productRecord = new ProductRecord(product, productDTO.getQuantity(), volume);
            volume.addProduct(productRecord);

            // Persist each product record
            entityManager.persist(productRecord);
        }

        return order;
    }

    private void validateOrderCreation(OrderCreateDTO orderCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        if (entityManager.find(Order.class, orderCreateDTO.getId()) != null) {
            throw new MyEntityExistsException("Order (" + orderCreateDTO.getId() + ") already exists");
        }

        var customer = entityManager.find(Customer.class, orderCreateDTO.getCustomerUsername());
        if (customer == null) {
            throw new MyEntityNotFoundException("Customer with username " + orderCreateDTO.getCustomerUsername() + " not found");
        }

        var volumeDTO = orderCreateDTO.getVolume();
        var packageType = entityManager.find(PackageType.class, volumeDTO.getPackageTypeId());
        if (packageType == null) {
            throw new MyEntityNotFoundException("PackageType with id " + volumeDTO.getPackageTypeId() + " not found");
        }

        if (entityManager.find(Volume.class, volumeDTO.getId()) != null) {
            throw new MyEntityExistsException("Volume with id " + volumeDTO.getId() + " already exists");
        }

        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
            if (entityManager.find(SensorType.class, sensorDTO.getSensorTypeId()) == null) {
                throw new MyEntityNotFoundException("SensorType not found for id: " + sensorDTO.getSensorTypeId());
            }
            if (entityManager.find(Sensor.class, sensorDTO.getId()) != null) {
                throw new MyEntityExistsException("Sensor with id " + sensorDTO.getId() + " already exists");
            }
        }

        for (ProductRecordDTO productDTO : volumeDTO.getProducts()) {
            if (entityManager.find(ProductType.class, productDTO.getProductId()) == null) {
                throw new MyEntityNotFoundException("ProductType not found for id: " + productDTO.getProductId());
            }
        }
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

}
