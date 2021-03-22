/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

import dk.dbc.httpclient.FailSafeHttpClient;
import dk.dbc.httpclient.HttpPost;
import dk.dbc.invariant.InvariantUtil;
import dk.dbc.util.Stopwatch;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AutoNomenConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoNomenConnector.class);

    private static final String NAMES_SUGGEST = "/api/names/suggest";
    private static final RetryPolicy<Response> RETRY_POLICY = new RetryPolicy<Response>()
            .handle(ProcessingException.class)
            .handleResultIf(response ->
                    response.getStatus() == 404
                            || response.getStatus() == 500
                            || response.getStatus() == 502)
            .withDelay(Duration.ofSeconds(5))
            .withMaxRetries(3);
    private final FailSafeHttpClient failSafeHttpClient;
    private final String baseUrl;

    public AutoNomenConnector(Client httpClient, String baseUrl) {
        this(FailSafeHttpClient.create(httpClient, RETRY_POLICY), baseUrl);
    }

    /**
     * Returns new instance with custom retry policy
     *
     * @param failSafeHttpClient web resources client with custom retry policy
     * @param baseUrl            base URL for record service endpoint
     */
    public AutoNomenConnector(FailSafeHttpClient failSafeHttpClient, String baseUrl) {
        this.failSafeHttpClient = InvariantUtil.checkNotNullOrThrow(
                failSafeHttpClient, "failSafeHttpClient");
        this.baseUrl = InvariantUtil.checkNotNullNotEmptyOrThrow(
                baseUrl, "baseUrl");
    }

    public void close() {
        failSafeHttpClient.getClient().close();
    }

    public AutoNomenSuggestions getSuggestions(String articleId) throws AutoNomenConnectorException {
        final AutoNomenRequest request = new AutoNomenRequest();
        request.setArticleId(articleId);

        return postRequest(NAMES_SUGGEST, request, AutoNomenSuggestions.class);
    }

    private <S, T> T postRequest(String path, S data, Class<T> returnType) throws AutoNomenConnectorException {
        LOGGER.info("POST {} with data {}", path, data);
        final Stopwatch stopwatch = new Stopwatch();
        try {
            final HttpPost httpPost = new HttpPost(failSafeHttpClient)
                    .withBaseUrl(baseUrl)
                    .withPathElements(path)
                    .withJsonData(data)
                    .withHeader("Accept", "application/json")
                    .withHeader("Content-type", "application/json");
            final Response response = httpPost.execute();
            assertResponseStatus(response, Response.Status.OK);
            return readResponseEntity(response, returnType);
        } finally {
            LOGGER.debug("POST {} took {} milliseconds", path,
                    stopwatch.getElapsedTime(TimeUnit.MILLISECONDS));
        }
    }

    private <T> T readResponseEntity(Response response, Class<T> type)
            throws AutoNomenConnectorException {
        final T entity = response.readEntity(type);
        if (entity == null) {
            throw new AutoNomenConnectorException(
                    String.format("Author Name Suggester service returned with null-valued %s entity",
                            type.getName()));
        }
        return entity;
    }

    private void assertResponseStatus(Response response, Response.Status expectedStatus)
            throws AutoNomenConnectorException {
        final Response.Status actualStatus =
                Response.Status.fromStatusCode(response.getStatus());
        if (actualStatus != expectedStatus) {
            if (actualStatus == Response.Status.NOT_FOUND) {
                final String message = response.readEntity(String.class);
                if (!message.isEmpty() && message.startsWith("Article")) {
                    throw new AutoNomenConnectorNotFoundException(message);
                }
            }
            throw new AutoNomenConnectorUnexpectedStatusCodeException(
                    String.format("auto-nomen service returned with unexpected status code: %s",
                            actualStatus),
                    actualStatus.getStatusCode());
        }
    }
}
