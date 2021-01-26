/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

public class AutoNomenConnectorTestWireMockRecorder {
     /*
        Steps to reproduce wiremock recording:

        * Start standalone runner
            java -jar wiremock-standalone-{WIRE_MOCK_VERSION}.jar --proxy-all="{AUTO_NOMEN_BASE_URL}" --record-mappings --verbose

        * Run the main method of this class

        * Replace content of src/test/resources/{__files|mappings} with that produced by the standalone runner
     */

    public static void main(String[] args) throws AutoNomenConnectorException {
        AutoNomenConnectorTest.connector = new AutoNomenConnector(
                AutoNomenConnectorTest.CLIENT, "http://localhost:8080");
        final AutoNomenConnectorTest autoNomenConnectorTest = new AutoNomenConnectorTest();
        allTests(autoNomenConnectorTest);
    }

    private static void allTests(AutoNomenConnectorTest autoNomenConnectorTest) throws AutoNomenConnectorException {
        autoNomenConnectorTest.getSuggestionsTestNoAuthors();
        autoNomenConnectorTest.getSuggestionsTestSingleAuthor();
        autoNomenConnectorTest.getSuggestionsTestTwoAuthors();
        autoNomenConnectorTest.getSuggestionsTestError();
    }
}
