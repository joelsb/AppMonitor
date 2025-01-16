package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.SensorRecord;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorRecordDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Your/Timezone")
    private Date time;
    private Double value;
    private Long sensorId;

    public SensorRecordDTO() {
    }

    public SensorRecordDTO(Long id, Date time, Double value, Long sensorId) {
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

    public static SensorRecordDTO fromSimple(SensorRecord sensorRecord) {
        return new SensorRecordDTO(
                null,
                sensorRecord.getTime(),
                sensorRecord.getValue(),
                null
        );
    }

    public static List<SensorRecordDTO> fromSimple(List<SensorRecord> sensorRecords) {
        return sensorRecords.stream().map(SensorRecordDTO::fromSimple).collect(Collectors.toList());
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
}
