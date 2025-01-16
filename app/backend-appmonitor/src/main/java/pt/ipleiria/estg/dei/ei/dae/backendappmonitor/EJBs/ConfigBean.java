package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import jakarta.inject.Inject;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Security.Hasher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    @EJB
    private XLSXFileBean xlsxFileBean;

    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        //create Entities
        try {
            if(xlsxFileBean.isFile("Users") && xlsxFileBean.isFile("ProductTypes") && xlsxFileBean.isFile("SensorTypes") && xlsxFileBean.isFile("PackageTypes")){
                System.out.println("Populating DB from XLSX files");
                xlsxFileBean.loadAllFromXlsx();
            }
            else {
                //Users creation
                System.out.println("Populating DB from scratch");
                if (xlsxFileBean.isFile("Users")) {
                    System.out.println("Populating Users from XLSX file");
                    xlsxFileBean.loadAllUsersFromXlsx();
                } else {
                    System.out.println("Populating Users from scratch");
                    var customerJoel = customerBean.create("Joel", hasher.hash("123"), "Joel", "joelsb@mail.com");
                    var customerTiago = customerBean.create("Tiago", hasher.hash("123"), "Tiago", "tiago@mail.com");
                    var employeeJose = employeeBean.create("Jose", hasher.hash("123"), "Jose", "jose@mail.com", "warehouse1");
                    var managerAna = managerBean.create("Ana", hasher.hash("123"), "Ana", "ana@mail.com", "office1");
                }

                //Product-Type creation
                if (xlsxFileBean.isFile("ProductTypes")) {
                    System.out.println("Populating ProductTypes from XLSX file");
                    xlsxFileBean.loadAllProductTypesFromXlsx();
                } else {
                    System.out.println("Populating ProductTypes from scratch");
                    var product1 = productTypeBean.create(1, "Televisao LCD Samsung", false);
                    var product2 = productTypeBean.create(2, "Gelado OLA - Corneto morango", true);
                }

                //Sensor-Type creation
                if (xlsxFileBean.isFile("SensorTypes")) {
                    System.out.println("Populating SensorTypes from XLSX file");
                    xlsxFileBean.loadAllSensorTypesFromXlsx();
                } else {
                    System.out.println("Populating SensorTypes from scratch");
                    var sensorTemperature = sensorTypeBean.create(1, "Temperature", "ºC", 30, 10);
                    var sensorHumidity = sensorTypeBean.create(2, "Humidity", "%", 80, 20);
                }

                //package-type creation
                if (xlsxFileBean.isFile("PackageTypes")) {
                    System.out.println("Populating PackageTypes from XLSX file");
                    xlsxFileBean.loadAllPackageTypesFromXlsx();
                } else {
                    System.out.println("Populating PackageTypes from scratch");
                    var packageCaixaIsotermicaS = packageTypeBean.create(1, "Caixa Isotermica S");
                    var packageCaixaIsotermicaM = packageTypeBean.create(2, "Caixa Isotermica M");
                    var packageCaixaIsotermicaL = packageTypeBean.create(3, "Caixa Isotermica L");
                    var packageCaixaIsotermicaXL = packageTypeBean.create(4, "Caixa Isotermica XL");
                    var packageCaixaCartaoS = packageTypeBean.create(5, "Caixa Cartao S");
                    var packageCaixaCartaoM = packageTypeBean.create(6, "Caixa Cartao M");
                    var packageCaixaCartaoL = packageTypeBean.create(7, "Caixa Cartao L");
                    var packageCaixaCartaoXL = packageTypeBean.create(8, "Caixa Cartao XL");
                    var packageBox = packageTypeBean.create(9, "Box");
                }
            }
            //add a mandatory sensor to a package-type
            packageTypeBean.addMandatorySensor(1L, 1L);
            packageTypeBean.addMandatorySensor(1L, 2L);

            //add a mandatory sensor to a product-type
            productTypeBean.addMandatorySensor(1L, 1L);
            productTypeBean.addMandatorySensor(2L, 2L);

            xlsxFileBean.saveAllToXlsx();
            System.out.println("Populating DB finished");
        } catch (Exception e) {
            logger.severe("ERROR Creating Entities: " + e.getMessage());
        }
        //create Order
        try {
            //ORDER CREATION
            /* For VolumeDTO:
            "id": 103,
            "sentDate": "2024-11-21T21:00:00",
            "packageTypeId": 2,
            "products": [
              {
                "productId": 1,
                "quantity": 1
              },
              {
                "productId": 2,
                "quantity": 3
              }
            ],
            "sensors": [
              {
                "id": 3,
                "sensorTypeId": 1
              },
              {
                "id": 4,
                "sensorTypeId": 2
              }
            ]
            */

            var sentDate1 = "2024-11-21T21:00:00";
            // Define the formatter to match the input format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            // Parse the string into a LocalDateTime object
            LocalDateTime sentDate2 = LocalDateTime.parse(sentDate1, formatter);
            // Convert LocalDateTime to Date
            Date sentDate = Date.from(sentDate2.atZone(ZoneId.systemDefault()).toInstant());
            //Sensors for packageTypeId 1 -> 1,2
            //Sensors for productTypeId 1 -> 1
            //Sensors for productTypeId 2 -> 2
            //Total sensorsTypeId -> 1,2,1,2
            var volumeCreateDTO = getVolumeDTO(sentDate, 103L, null, null,
                    List.of(Map.entry(1L, 1), Map.entry(1L, 1)),  //products (Id, Quantity)
                    List.of(Map.entry(3L, 1L), Map.entry(4L, 1L)));  //sensors (Id, SensorTypeId)

            /* For OrderCreateDTO:
              "id": 28,
              "customerUsername": "Tiago",
              "createdDate": "2024-11-20T21:00:00",
              "volume": {
            */
            String createdDate1 = "2024-11-20T21:00:00";
            LocalDateTime createdDate2 = LocalDateTime.parse(createdDate1, formatter);
            Date createdDate = Date.from(createdDate2.atZone(ZoneId.systemDefault()).toInstant());

            var orderCreateDTO = new OrderCreateDTO(28L, "Joel", createdDate, volumeCreateDTO);
            var order = orderBean.create(orderCreateDTO);

        } catch (Exception e) {
            logger.severe("Creating Order: " + e.getMessage());
        }
        //create Volume
//        try {
//            /*
//            {
//              "id": 105,
//              "sentDate": "2021-06-01T00:00:00",
//              "orderId": 27,
//              "packageTypeId": 1,
//              "products": [
//                {
//                  "productId": 1,
//                  "quatity": 1
//                },
//                {
//                  "productId": 2,
//                  "quatity": 3
//                }
//              ],
//              "sensors": [
//                {
//                  "id": 1,
//                  "sensorTypeId": 1
//                },
//                {
//                  "id": 2,
//                  "sensorTypeId": 2
//                }
//              ]
//            }
//             */
//            var orderId = 28L;
//            var sentDate1 = "2021-06-01T00:00:00";
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//            LocalDateTime sentDate2 = LocalDateTime.parse(sentDate1, formatter);
//            Date sentDate = Date.from(sentDate2.atZone(ZoneId.systemDefault()).toInstant());
//
//            var volumeCreateDTO = getVolumeDTO(sentDate, 106, 1, orderId, List.of(Map.entry(1L, 1), Map.entry(2L, 3)), List.of(Map.entry(1L, 1L), Map.entry(2L, 2L)));
//
//            var volume = volumeBean.addVolumeToOrder(volumeCreateDTO);
//        } catch (Exception e) {
//            logger.severe("Creating Volume: " + e.getMessage());
//        }

    }


    private static VolumeCreateDTO getVolumeDTO(Date sentDate, Long volumeId, Long packageTypeId, Long orderId, List<Map.Entry<Long, Integer>> products, List<Map.Entry<Long, Long>> sensors) {
        // Create VolumeCreateDTO with initial data
        var volumeCreateDTO = new VolumeCreateDTO(volumeId, sentDate, null, orderId, packageTypeId);

        // Map product data to ProductRecordDTO
        var productRecords = products.stream().map(entry -> new ProductRecordDTO(null, entry.getKey(), entry.getValue(), null, null)).collect(Collectors.toList());

        // Map sensor data to SensorDTO
        var sensorDTOs = sensors.stream().map(entry -> new SensorDTO(entry.getKey(), entry.getValue(), null, null)).collect(Collectors.toList());

        // Set products and sensors in VolumeCreateDTO
        volumeCreateDTO.setProducts(productRecords);
        volumeCreateDTO.setSensors(sensorDTOs);

        return volumeCreateDTO;
    }
}
