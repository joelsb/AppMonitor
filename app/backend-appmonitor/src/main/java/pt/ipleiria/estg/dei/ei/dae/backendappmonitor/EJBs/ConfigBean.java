package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.logging.Logger;

@Singleton
@Startup

public class ConfigBean {
    @EJB
    private UserBean userBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {


        try {
            userBean.create("joel123", "123", "Joel", "joel@mail.com");

        }
        catch (Exception e) {
            logger.severe(e.getMessage());
        }

    }
}
