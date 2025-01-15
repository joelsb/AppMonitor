package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.*;
import jakarta.persistence.*;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@Stateless
public class XLSXFileBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private CustomerBean customerBean;
    @EJB
    private ManagerBean managerBean;
    @EJB
    private EmployeeBean employeeBean;
    @EJB
    private ProductTypeBean productTypebean;
    @EJB
    private PackageTypeBean packageTypebean;
    @EJB
    private SensorTypeBean sensorTypeBean;

    // Manually go up the directory tree from the server's working directory
    String userDir = System.getProperty("user.dir");
    String fileLocation = userDir + "/";

    public boolean isFile(String name) {
        return Files.exists(Paths.get(fileLocation + name + ".xlsx"));
    }

    public void saveAllUsersToXlsx() {
        // Retrieve all users from the databasev
        List<User> users = entityManager.createNamedQuery("getAllUsers", User.class).getResultList();

        String location = fileLocation + "Users.xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(location));
             Workbook wb = new Workbook(os, "MonitorApplication", "1.0")) {

            Worksheet ws = wb.newWorksheet("Users");

            // Setting column widths
            ws.width(0, 20);  // Role (Customer, Manager, Employee)
            ws.width(1, 25);  // Name
            ws.width(2, 30);  // Email
            ws.width(3, 25);  // Username
            //a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3
            ws.width(4, 65);  // Password
            ws.width(5, 25);  // Office (only for Managers)
            ws.width(6, 25);  // Warehouse (only for Employees)

            // Writing headers
            ws.range(0, 0, 0, 6).style().fontName("Arial").fontSize(16).bold().fillColor("3366FF").set();
            ws.value(0, 0, "Role");
            ws.value(0, 1, "Name");
            ws.value(0, 2, "Email");
            ws.value(0, 3, "Username");
            ws.value(0, 4, "Password");
            ws.value(0, 5, "Office");
            ws.value(0, 6, "Warehouse");

            // Writing data rows for each user
            int row = 1;
            for (User user : users) {
                // Writing to the correct columns based on the user type
                if (user instanceof Customer) {
                    Customer customer = (Customer) user;
                    ws.value(row, 0, "Customer"); // Role
                    ws.value(row, 1, customer.getName()); // Name
                    ws.value(row, 2, customer.getEmail()); // Email
                    ws.value(row, 3, customer.getUsername()); // Username
                    ws.value(row, 4, customer.getPassword()); // Password
                    ws.value(row, 5, ""); // Office (for Customer, no office)
                    ws.value(row, 6, ""); // Warehouse (for Customer, no warehouse)
                } else if (user instanceof Manager) {
                    Manager manager = (Manager) user;
                    ws.value(row, 0, "Manager"); // Role
                    ws.value(row, 1, manager.getName()); // Name
                    ws.value(row, 2, manager.getEmail()); // Email
                    ws.value(row, 3, manager.getUsername()); // Username
                    ws.value(row, 4, manager.getPassword()); // Password
                    ws.value(row, 5, manager.getOffice()); // Office (for Manager)
                    ws.value(row, 6, ""); // Warehouse (for Manager, no warehouse)
                } else if (user instanceof Employee) {
                    Employee employee = (Employee) user;
                    ws.value(row, 0, "Employee"); // Role
                    ws.value(row, 1, employee.getName()); // Name
                    ws.value(row, 2, employee.getEmail()); // Email
                    ws.value(row, 3, employee.getUsername()); // Username
                    ws.value(row, 4, employee.getPassword()); // Password
                    ws.value(row, 5, ""); // Office (for Employee, no office)
                    ws.value(row, 6, employee.getWarehouse()); // Warehouse (for Employee)
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Error creating the Excel file for Users: " + e.getMessage());
        }
    }

    public void saveAllProductTypesToXlsx() {
        // Retrieve all product types from the database
        List<ProductType> productTypes = entityManager.createNamedQuery("getAllProductTypes", ProductType.class).getResultList();
        String location = fileLocation + "ProductTypes.xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(location));
             Workbook wb = new Workbook(os, "MonitorApplication", "1.0")) {

            Worksheet ws = wb.newWorksheet("ProductTypes");
            //id (long), mandatoryPackage (boolean), name
            // Setting column widths
            ws.width(0, 10);  // ID
            ws.width(1, 15);  // Mandatory Package
            ws.width(2, 25);  // Name

            // Writing headers
            ws.range(0, 0, 0, 2).style().fontName("Arial").fontSize(16).bold().fillColor("3366FF").set();
            ws.value(0, 0, "ID");
            ws.value(0, 1, "Mandatory Package");
            ws.value(0, 2, "Name");

            // Writing data rows for each product type
            int row = 1;
            for (ProductType productType : productTypes) {
                ws.value(row, 0, productType.getId()); // ID
                ws.value(row, 1, productType.isMandatoryPackage()); // Mandatory Package
                ws.value(row, 2, productType.getName()); // Name
                row++;
            }

        } catch (IOException e) {
            System.out.println("Error creating the Excel file for Product Types: " + e.getMessage());
        }

    }

    public void saveAllPackageTypesToXlsx() {
        List<PackageType> packageTypes = entityManager.createNamedQuery("getAllPackageTypes", PackageType.class).getResultList();

        String location = fileLocation + "PackageTypes.xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(location));
             Workbook wb = new Workbook(os, "MonitorApplication", "1.0")) {

            Worksheet ws = wb.newWorksheet("PackageTypes");
            //id (long), name
            // Setting column widths
            ws.width(0, 10);  // ID
            ws.width(1, 25);  // Name

            // Writing headers
            ws.range(0, 0, 0, 1).style().fontName("Arial").fontSize(16).bold().fillColor("3366FF").set();
            ws.value(0, 0, "ID");
            ws.value(0, 1, "Name");

            // Writing data rows for each package type
            int row = 1;
            for (PackageType packageType : packageTypes) {
                ws.value(row, 0, packageType.getId()); // ID
                ws.value(row, 1, packageType.getName()); // Name
                row++;
            }

        } catch (IOException e) {
            System.out.println("Error creating the Excel file for Package Types: " + e.getMessage());
        }
    }

    public void saveAllSensorTypesToXlsx() {
        List<SensorType> sensorTypes = entityManager.createNamedQuery("getAllSensorTypes", SensorType.class).getResultList();
        String location = fileLocation + "SensorTypes.xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(location));
             Workbook wb = new Workbook(os, "MonitorApplication", "1.0")) {

            Worksheet ws = wb.newWorksheet("SensorTypes");
            //id (long), name, unit, maxValue, minValue
            // Setting column widths
            ws.width(0, 10);  // ID
            ws.width(1, 25);  // Name
            ws.width(2, 10);  // Unit
            ws.width(3, 10);  // Max Value
            ws.width(4, 10);  // Min Value

            // Writing headers
            ws.range(0, 0, 0, 4).style().fontName("Arial").fontSize(16).bold().fillColor("3366FF").set();
            ws.value(0, 0, "ID");
            ws.value(0, 1, "Name");
            ws.value(0, 2, "Unit");
            ws.value(0, 3, "Ceeling");
            ws.value(0, 4, "Floor");

            // Writing data rows for each sensor type
            int row = 1;
            for (SensorType sensorType : sensorTypes) {
                ws.value(row, 0, sensorType.getId()); // ID
                ws.value(row, 1, sensorType.getName()); // Name
                ws.value(row, 2, sensorType.getUnit()); // Unit
                ws.value(row, 3, sensorType.getCeiling()); // Max Value
                ws.value(row, 4, sensorType.getFloor()); // Min Value
                row++;
            }

        } catch (IOException e) {
            System.out.println("Error creating the Excel file for Sensor Types: " + e.getMessage());
        }
    }

    public void saveAllToXlsx() {
        // Save all entities to the XLSX file
        try {
            // Save Users
            saveAllUsersToXlsx();
            // Save Product Types
            saveAllProductTypesToXlsx();
            // Save Package Types
            saveAllPackageTypesToXlsx();
            // Save Sensor Types
            saveAllSensorTypesToXlsx();
        } catch (Exception e) {
            System.out.println("Error saving entities to XLSX file: " + e.getMessage());
        }
    }

    public void loadAllUsersFromXlsx() {
        String location = fileLocation + "Users.xlsx";
        try (FileInputStream file = new FileInputStream(location);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {
            // Get sheet by name "Users"
            Sheet sheet = wb.getFirstSheet();

            // Check if the sheet exists

            // Iterate over the rows and extract user data
            try (Stream<Row> rows = sheet.openStream()) {
                Iterator<Row> rowIterator = rows.iterator();
                // Skip header row
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Skip first row (headers)
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        rowData.add(cell.getRawValue());
                    }

                    // Process the row data based on the expected columns
                    String role = rowData.get(0); // Role (Customer, Manager, Employee)
                    String name = rowData.get(1); // Name
                    String email = rowData.get(2); // Email
                    String username = rowData.get(3); // Username
                    String password = rowData.get(4); // Password
                    String office = rowData.get(5); // Office (Manager)
                    String warehouse = rowData.get(6); // Warehouse (Employee)

                    // Create appropriate User object based on role
                    if ("Customer".equals(role)) {
                        customerBean.create(username, password, name, email);
                    } else if ("Manager".equals(role)) {
                        managerBean.create(username, password, name, email, office);
                    } else if ("Employee".equals(role)) {
                        employeeBean.create(username, password, name, email, warehouse);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading the Excel file: " + e.getMessage());
            } catch (MyEntityExistsException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllProductTypesFromXlsx() throws IOException {
        String location = fileLocation + "ProductTypes.xlsx";
        try (FileInputStream file = new FileInputStream(location);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {

            // Get sheet by name "ProductTypes"
            Sheet sheet = wb.getFirstSheet();

            // Iterate over the rows and extract product type data
            try (Stream<Row> rows = sheet.openStream()) {
                Iterator<Row> rowIterator = rows.iterator();
                // Skip header row
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Skip first row (headers)
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        rowData.add(cell.getRawValue());
                    }

                    // Process the row data based on the expected columns
                    String id = rowData.get(0); // ID
                    String mandatoryPackage = rowData.get(1); // Mandatory Package
                    String name = rowData.get(2); // Name

                    // Create appropriate ProductType object
                    boolean isMandatoryPackage = "true".equalsIgnoreCase(mandatoryPackage);

                    productTypebean.create(Long.parseLong(id), name, isMandatoryPackage);
                }
            } catch (IOException e) {
                System.out.println("Error reading the Excel file: " + e.getMessage());
            } catch (MyEntityExistsException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadAllPackageTypesFromXlsx() throws IOException {
        String location = fileLocation + "PackageTypes.xlsx";
        try (FileInputStream file = new FileInputStream(location);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {

            // Get sheet by name "PackageTypes"
            Sheet sheet = wb.getFirstSheet();


            // Iterate over the rows and extract package type data
            try (Stream<Row> rows = sheet.openStream()) {
                Iterator<Row> rowIterator = rows.iterator();
                // Skip header row
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Skip first row (headers)
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        rowData.add(cell.getRawValue());
                    }

                    // Process the row data based on the expected columns
                    String id = rowData.get(0); // ID
                    String name = rowData.get(1); // Name

                    // Create appropriate PackageType object
                    packageTypebean.create(Long.parseLong(id), name);
                }
            } catch (IOException e) {
                System.out.println("Error reading the Excel file: " + e.getMessage());
            } catch (MyEntityExistsException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadAllSensorTypesFromXlsx() throws IOException {
        String location = fileLocation + "SensorTypes.xlsx";
        try (FileInputStream file = new FileInputStream(location);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {

            // Get sheet by name "SensorTypes"
            Sheet sheet = wb.getFirstSheet();

            // Iterate over the rows and extract sensor type data
            try (Stream<Row> rows = sheet.openStream()) {
                Iterator<Row> rowIterator = rows.iterator();
                // Skip header row
                if (rowIterator.hasNext()) {
                    rowIterator.next(); // Skip first row (headers)
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        rowData.add(cell.getRawValue());
                    }

                    // Process the row data based on the expected columns
                    String id = rowData.get(0); // ID
                    String name = rowData.get(1); // Name
                    String unit = rowData.get(2); // Unit
                    String ceiling = rowData.get(3); // Max Value
                    String floor = rowData.get(4); // Min Value

                    sensorTypeBean.create(Long.parseLong(id), name, unit, Double.parseDouble(ceiling), Double.parseDouble(floor));
                }
            } catch (IOException e) {
                System.out.println("Error reading the Excel file: " + e.getMessage());
            } catch (MyEntityExistsException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadAllFromXlsx() throws IOException {
        // Load all entities from the XLSX file
        try {
            // Load Users
            if (isFile("Users")) {
                loadAllUsersFromXlsx();
            }
            // Load Product Types
            if (isFile("ProductTypes")) {
                loadAllProductTypesFromXlsx();
            }
            // Load Package Types
            if (isFile("PackageTypes")) {
                loadAllPackageTypesFromXlsx();
            }
            // Load Sensor Types
            if (isFile("SensorTypes")) {
                loadAllSensorTypesFromXlsx();
            }
        } catch (Exception e) {
            System.out.println("Error loading entities from XLSX file: " + e.getMessage());
        }
    }
}
