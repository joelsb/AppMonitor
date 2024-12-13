package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO {
    /*
    id: long
    sensorType: SensorType
    volume: Volume
    history: List<HistoryRecord> (não detalhado no diagrama)
     */

    private long id;
    private long sensorTypeId;
    private Long volumeId;
    private List<SensorRecordDTO> history = new ArrayList<>();
    private boolean excludeHistory = true;

    public SensorDTO() {
    }

    public SensorDTO(long id, long sensorTypeId, Long volumeId) {
        this.id = id;
        this.sensorTypeId = sensorTypeId;
        this.volumeId = volumeId;
    }

    public static SensorDTO from(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getSensorType().getId(),
                null
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorDTO::from).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSensorTypeId() {
        return sensorTypeId;
    }

    public void setSensorTypeId(long sensorTypeId) {
        this.sensorTypeId = sensorTypeId;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }

    public List<SensorRecordDTO> getHistory() {
        return excludeHistory ? null : new ArrayList<>(history);
    }

    public void setHistory(List<SensorRecordDTO> history) {
        this.history = history;
    }
}
