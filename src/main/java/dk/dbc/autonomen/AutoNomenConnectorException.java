/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

public class AutoNomenConnectorException extends Exception {

    public AutoNomenConnectorException(String message) {
        super(message);
    }

    public AutoNomenConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
