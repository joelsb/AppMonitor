package pt.ipleiria.estg.dei.ei.dae.backendappmonitor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Set;


@ApplicationPath("/api") // Base URI for all JAX-RS resources
public class MonitorApplication extends Application {

}
