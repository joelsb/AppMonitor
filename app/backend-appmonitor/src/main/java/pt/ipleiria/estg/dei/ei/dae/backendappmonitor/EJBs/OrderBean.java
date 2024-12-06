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
        if(order == null) {
            throw new MyEntityNotFoundException("Order (" + id + ") not found");
        }
        return order;
    }

    public List<Order> findAll() {
        return entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
    }

    public Order create(OrderCreateDTO orderCreateDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        //Deal when order already exists
        var order = entityManager.find(Order.class, orderCreateDTO.getId());
        if(order!=null) {
            throw new MyEntityExistsException("Order (" + orderCreateDTO.getId() + ") already exists");
        }
        var orderId = orderCreateDTO.getId();
        var customerUsername = orderCreateDTO.getCustomerUsername();
        var createdDate = orderCreateDTO.getCreatedDate();
        var volumeDTO = orderCreateDTO.getVolume();

        //Deal when order does not exist
        var customer = entityManager.find(Customer.class, customerUsername);
        order = new Order(
                orderId, createdDate, null,customer);
        customer.addOrder(order);

        //call the function create at VolumeBean
        var volumeCreateDTO = new VolumeCreateDTO(
                volumeDTO.getId(),
                volumeDTO.getSentDate(),
                volumeDTO.getPackageTypeId(),
                orderId
        );
        volumeCreateDTO.setProducts(volumeDTO.getProducts());
        volumeCreateDTO.setSensors(volumeDTO.getSensors());

        var volume = volumeBean.create(volumeCreateDTO, order);
        order.addVolume(volume);

//        //Create the Product Records
//        //for each product in getProducts, create a new Product Record
//        for (ProductRecordDTO productDTO : volumeDTO.getProducts()) {
//            var product = entityManager.find(ProductType.class, productDTO.getProductId());
//            if (product == null) {
//                throw new MyEntityNotFoundException("ProductType not found for id: " + productDTO.getProductId());
//            }
//            var productRecord = new ProductRecord(product, productDTO.getQuantity(), volume);
//            //Product_id em ProductRecord esta a null
//
//            volume.addProduct(productRecord);
//        }
//
//        //Create the sensors
//        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
//            var sensorType = entityManager.find(SensorType.class, sensorDTO.getSensorTypeId());
//            if (sensorType == null) {
//                throw new MyEntityNotFoundException("SensorType not found for id: " + sensorDTO.getSensorTypeId());
//            }
//            var sensor = new Sensor(sensorDTO.getId(), sensorType, volume);
//            volume.addSensor(sensor);
//        }

        //persist everything
        entityManager.persist(order);
//        entityManager.persist(volume);

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
            if(volume == null) {
                throw new MyEntityNotFoundException("Volume (" + volumeId + ") not found");
            }
            else {
                if(order.getVolumes().contains(volume)){
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
}
