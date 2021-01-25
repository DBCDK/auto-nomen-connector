/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExactMatchNames extends AutoNomenSuggestion {
    private Map<String, String> fields;

    @JsonProperty("term-fo")
    private String termFo;

    private static String newXmlSafeKey(String key) {
        if (!key.isEmpty()) {
            return "field" + key;
        }
        return key;
    }

    @JsonProperty
    public Map<String, String> getFields() {
        if (fields != null) {
            return fields.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> ExactMatchNames.newXmlSafeKey(e.getKey()),
                            Map.Entry::getValue));
        }
        return null;
    }

    public void setFields(Map<String, String> fields) {
        if (fields != null) {
            this.fields = new HashMap<>(fields);
        }
    }

    public String getTermFo() {
        return termFo;
    }

    public void setTermFo(String termFo) {
        this.termFo = termFo;
    }
}
