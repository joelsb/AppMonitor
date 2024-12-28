package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Order;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.ProductRecord;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Volume;

import java.util.Date;

import java.util.List;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private CustomerBean customerBean;
    @EJB
    private ManagerBean managerBean;
    @EJB
    private EmployeeBean employeeBean;
    @EJB
    private ProductTypeBean productTypeBean;
    @EJB
    private SensorTypeBean sensorTypeBean;
    @EJB
    private OrderBean orderBean;
    @EJB
    private SensorBean sensorBean;
    @EJB
    private VolumeBean volumeBean;
    @EJB
    private PackageTypeBean packageTypeBean;
    @EJB
    private ProductRecordBean productRecordBean;
    @EJB
    private SensorRecordBean sensorRecordBean;



    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try {
            System.out.println("Populating DB");
            //Users creation
            customerBean.create("Joel", "123", "Joel", "joelsb@mail.com");
            var customer = customerBean.create("Tiago", "123", "Tiago", "tiago@mail.com");
            employeeBean.create("Jose", "123", "Jose", "jose@mail.com", "warehouse1");
            managerBean.create("Ana", "123", "Ana", "ana@mail.com", "office1");

            //Product-Type creation
            productTypeBean.create("Televisao LCD Samsung", false);

            var productType = productTypeBean.create("Gelado OLA - Corneto morango", true);

            //Sensor-Type creation
            var sensorTemperature = sensorTypeBean.create("Temperature", "ºC", 30, 10);
            var sensorHumidity = sensorTypeBean.create("Humidity", "%", 80, 20);



            //add a mandatory sensor to a product-type
            //add the mandatory sensor Temperature to the product-type Televisao LCD Samsung
            productTypeBean.addMandatorySensor(1L, 1L);
            //add the mandatory sensors Humidity and Temperature to the product-type Gelado OLA - Corneto morango
            productTypeBean.addMandatorySensor(2L, 1L);
            productTypeBean.addMandatorySensor(2L, 2L);

            //Package-Type creation
            var pack = packageTypeBean.create("Box");

            //Order creation
            Date date = new Date();
            var order = orderBean.create(date,"Tiago");




            //Volume creation
            var product1 = productRecordBean.create(productType, 1,null);
            var product2 = productRecordBean.create(productType, 1,null);
            var products = List.of(product1, product2);

            var sensor1 = sensorBean.create(sensorHumidity.getId(), null);
            var sensor2 = sensorBean.create(sensorTemperature.getId(), null);
            var sensors = List.of(sensor1, sensor2);
            var sensorRecord1 = sensorRecordBean.create(date, 10,sensor1);
            var sensorRecord2 = sensorRecordBean.create(date, 20,sensor2);
            var volume = volumeBean.create(date,pack,products, sensors, order);


            //package-type creation
            packageTypeBean.create("Caixa Isotermica S");
            packageTypeBean.create("Caixa Isotermica M");
            packageTypeBean.create("Caixa Isotermica L");
            packageTypeBean.create("Caixa Isotermica XL");
            packageTypeBean.create("Caixa Cartao S");
            packageTypeBean.create("Caixa Cartao M");
            packageTypeBean.create("Caixa Cartao L");
            packageTypeBean.create("Caixa Cartao XL");

            //add a mandatory sensor to a package-type
            packageTypeBean.addMandatorySensor(1L, 1L);
            packageTypeBean.addMandatorySensor(1L, 2L);


        }
        catch (Exception e) {
            logger.severe(e.getMessage());
        }

    }
}
