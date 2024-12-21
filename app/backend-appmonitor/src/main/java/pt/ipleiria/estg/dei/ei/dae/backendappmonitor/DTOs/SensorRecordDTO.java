package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SensorRecordDTO {
    /*
    id: long
    time: Date
    value: double
    sensor: Sensor
     */

    private Long id;
    private Date time;
    private double value;
    private Long sensorId;

    public SensorRecordDTO() {
    }

    public SensorRecordDTO(Long id, Date time, double value, Long sensorId) {
        this.id = id;
        this.time = time;
        this.value = value;
        this.sensorId = sensorId;
    }
    public static SensorRecordDTO from(SensorRecord sensorRecord) {
        return new SensorRecordDTO(
                sensorRecord.getId(),
                sensorRecord.getTime(),
                sensorRecord.getValue(),
                sensorRecord.getSensor().getId()
        );
    }

    public static SensorRecordDTO fromSensor(SensorRecord sensorRecord) {
        return new SensorRecordDTO(
                null,
                sensorRecord.getTime(),
                sensorRecord.getValue(),
                null
        );
    }

    public static List<SensorRecordDTO> fromSensor(List<SensorRecord> sensorRecords) {
        return sensorRecords.stream().map(SensorRecordDTO::fromSensor).collect(Collectors.toList());
    }

    public static List<SensorRecordDTO> from(List<SensorRecord> sensorRecords) {
        return sensorRecords.stream().map(SensorRecordDTO::from).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
}
