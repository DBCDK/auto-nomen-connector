/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

import com.github.tomakehurst.wiremock.WireMockServer;
import dk.dbc.httpclient.HttpClient;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class AutoNomenConnectorTest {
    private static WireMockServer wireMockServer;
    private static String wireMockHost;

    final static Client CLIENT = HttpClient.newClient(new ClientConfig()
            .register(new JacksonFeature()));
    static AutoNomenConnector connector;

    @BeforeAll
    static void startWireMockServer() {
        wireMockServer = new WireMockServer(options().dynamicPort()
                .dynamicHttpsPort());
        wireMockServer.start();
        wireMockHost = "http://localhost:" + wireMockServer.port();
        configureFor("localhost", wireMockServer.port());
    }

    @BeforeAll
    static void setConnector() {
        connector = new AutoNomenConnector(CLIENT, wireMockHost);
    }

    @AfterAll
    static void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Test
    void getSuggestionsTestNoAuthors() throws AutoNomenConnectorException {
        final AutoNomenSuggestions actual = connector.getSuggestions("e818b61d");

        assertThat(actual.getAutNames().size(), is(0));
        assertThat(actual.getNerNames().size(), is(0));
    }

    @Test
    void getSuggestionsTestSingleAuthor() throws AutoNomenConnectorException {
        final AutoNomenSuggestions actual = connector.getSuggestions("e6ec1122");

        assertThat(actual.getAutNames().size(), is(1));
        assertThat(actual.getAutNames().get(0).getInputName(), is("Jens Rebensdorff"));
        assertThat(actual.getAutNames().get(0).getAuthority(), is("19180557"));
        assertThat(actual.getNerNames().size(), is(0));
    }

    @Test
    void getSuggestionsTestTwoAuthors() throws AutoNomenConnectorException {
        final AutoNomenSuggestions actual = connector.getSuggestions("e80cefac");

        assertThat(actual.getAutNames().size(), is(2));
        assertThat(actual.getAutNames().get(0).getInputName(), is("Ida Sejersdal Dreiager"));
        assertThat(actual.getAutNames().get(0).getAuthority(), is("48654452"));
        assertThat(actual.getAutNames().get(1).getInputName(), is("Sophie Kaae Jørgensen"));
        assertThat(actual.getAutNames().get(1).getAuthority(), is("48820034"));
        assertThat(actual.getNerNames().size(), is(0));
    }

    @Test
    void getSuggestionsTestError() {
        Assertions.assertThrows(AutoNomenConnectorNotFoundException.class, () -> connector.getSuggestions(""), "Article could not be found.");
        Assertions.assertThrows(AutoNomenConnectorNotFoundException.class, () -> connector.getSuggestions("julemanden"), "Article julemanden could not be found.");
    }
}
