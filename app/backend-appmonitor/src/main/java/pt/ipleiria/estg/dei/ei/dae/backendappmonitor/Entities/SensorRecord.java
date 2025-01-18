package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Table(name = "sensorRecords")
@Entity
public class SensorRecord extends Versionable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private Date time;
    @NotNull
    private double value;
    @NotNull
    @ManyToOne
    private Sensor sensor;

    public SensorRecord() {
    }


    public SensorRecord(Date time, double value, Sensor sensor) {
        this.time = time;
        this.value = value;
        this.sensor = sensor;
    }

    public long getId() {
        return id;
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

}
