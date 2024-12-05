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

    private long id;
    private Date time;
    private double value;
    private long sensorId;

    public SensorRecordDTO() {
    }

    public SensorRecordDTO(long id, Date time, double value, long sensorId) {
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

    public List<SensorRecordDTO> from(List<SensorRecord> sensorRecords) {
        return sensorRecords.stream().map(SensorRecordDTO::from).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }
}
