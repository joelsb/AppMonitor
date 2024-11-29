package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;

import java.util.List;
import java.util.stream.Collectors;

public class SensorTypeDTO {
    /*
    id: long
    name: String
    unit: String
    ceiling: double
    floor: double
     */

    private long id;
    private String name;
    private String unit;
    private double ceiling;
    private double floor;

    public SensorTypeDTO() {
    }

    public SensorTypeDTO(long id, String name, String unit, double ceiling, double floor) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.ceiling = ceiling;
        this.floor = floor;
    }

    public static SensorTypeDTO from(SensorType sensorType) {
        return new SensorTypeDTO(
                sensorType.getId(),
                sensorType.getName(),
                sensorType.getUnit(),
                sensorType.getCeiling(),
                sensorType.getFloor()
        );
    }

    public List<SensorTypeDTO> from(List<SensorType> sensorTypes) {
        return sensorTypes.stream().map(SensorTypeDTO::from).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getCeiling() {
        return ceiling;
    }

    public void setCeiling(double ceiling) {
        this.ceiling = ceiling;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }
}
