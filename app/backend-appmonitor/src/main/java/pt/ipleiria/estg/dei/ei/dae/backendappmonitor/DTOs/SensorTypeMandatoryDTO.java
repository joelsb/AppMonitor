package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

public class SensorTypeMandatoryDTO {
    private long sensorId;

    public SensorTypeMandatoryDTO() {
    }

    public SensorTypeMandatoryDTO(long sensorId) {
        this.sensorId = sensorId;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }
}
