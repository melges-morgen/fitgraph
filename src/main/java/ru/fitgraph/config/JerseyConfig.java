package ru.fitgraph.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Created by melge on 12.03.2016.
 */
@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages(ru.fitgraph.BootMain.class.getPackage().toString());
    }
}
