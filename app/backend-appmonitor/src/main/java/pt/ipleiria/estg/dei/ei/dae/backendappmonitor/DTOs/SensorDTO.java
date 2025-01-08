package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorType;

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

    private Long id;
    private Long sensorTypeId;
    private SensorTypeDTO sensorType;
    private Long volumeId;
    private List<SensorRecordDTO> history = new ArrayList<>();

    public SensorDTO() {
    }

    public SensorDTO(Long id, Long sensorTypeId,SensorTypeDTO sensorType, Long volumeId,List<SensorRecordDTO> history) {
        this.id = id;
        this.sensorTypeId = sensorTypeId;
        this.sensorType = sensorType;
        this.volumeId = volumeId;
        this.history = history;
    }

    public static SensorDTO fromHistory(Sensor sensor) {
        return new SensorDTO(
                null,
                null,
                sensor.getSensorType()== null ? null : SensorTypeDTO.from(sensor.getSensorType()),
                null,
                sensor.getHistory() == null ? null : SensorRecordDTO.from(sensor.getHistory())
        );
    }

    public static SensorDTO from(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getSensorType() == null ? null : sensor.getSensorType().getId(),
                sensor.getSensorType() == null ? null : SensorTypeDTO.from(sensor.getSensorType()),
                null,
                sensor.getHistory() == null ? null : SensorRecordDTO.from(sensor.getHistory())
        );

    }

    public static List<SensorDTO> fromHistory(List<Sensor> sensors) {
        return sensors.stream().map(SensorDTO::fromHistory).collect(Collectors.toList());
    }

    public static List<SensorDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorDTO::from).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSensorTypeId() {
        return sensorTypeId;
    }

    public void setSensorTypeId(Long sensorTypeId) {
        this.sensorTypeId = sensorTypeId;
    }

    public SensorTypeDTO getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorTypeDTO sensorType) {
        this.sensorType = sensorType;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }

    public List<SensorRecordDTO> getHistory() {
        return history;
    }

    public void setHistory(List<SensorRecordDTO> history) {
        this.history = history;
    }
}
