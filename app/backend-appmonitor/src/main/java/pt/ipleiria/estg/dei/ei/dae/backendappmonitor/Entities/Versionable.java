package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.io.Serializable;

@MappedSuperclass
public class Versionable implements Serializable {
    @Version
    private int version;
}