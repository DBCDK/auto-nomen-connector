/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

import dk.dbc.httpclient.HttpClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AutoNomenConnector factory
 * <p>
 * Synopsis:
 * </p>
 * <pre>
 *    // New instance
 *    AutoNomenConnector connector = AutoNomenConnectorFactory.create("http://auto-nomen");
 *
 *    // Singleton instance in CDI enabled environment
 *    {@literal @}Inject
 *    AutoNomenConnectorFactory factory;
 *    ...
 *    AutoNomenConnector connector = factory.getInstance();
 *
 *    // or simply
 *    {@literal @}Inject
 *    AutoNomenConnector connector;
 * </pre>
 * <p>
 * The CDI case depends on the auto-nomen service base-url being defined as
 * the value of either a system property or environment variable
 * named AUTO_NOMEN_SERVICE_URL.
 * </p>
 */
@ApplicationScoped
public class AutoNomenConnectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoNomenConnectorFactory.class);

    public static AutoNomenConnector create(String baseUrl) throws AutoNomenConnectorException {
        final Client client = HttpClient.newClient(new ClientConfig()
                .register(new JacksonFeature()));
        LOGGER.info("Creating AutoNomenConnector for: {}", baseUrl);
        return new AutoNomenConnector(client, baseUrl);
    }

    @Inject
    @ConfigProperty(name = "AUTO_NOMEN_SERVICE_URL")
    private String baseUrl;

    AutoNomenConnector autoNomenConnector;

    @PostConstruct
    public void initializeConnector() {
        try {
            autoNomenConnector = AutoNomenConnectorFactory.create(baseUrl);
        } catch (AutoNomenConnectorException e) {
            throw new IllegalStateException(e);
        }
    }

    @Produces
    public AutoNomenConnector getInstance() {
        return autoNomenConnector;
    }

    @PreDestroy
    public void tearDownConnector() {
        autoNomenConnector.close();
    }
}
