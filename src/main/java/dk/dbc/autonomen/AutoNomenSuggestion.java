/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AutoNomenSuggestion {

    @JsonProperty("input-name")
    private String inputName;

    @JsonProperty("authority")
    private String authority;

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public static class Builder {
        private String inputName;
        private String authority;

        public Builder withInputName(String inputName) {
            this.inputName = inputName;
            return this;
        }

        public Builder withAuthority(String authority) {
            this.authority = authority;
            return this;
        }

        public AutoNomenSuggestion build() {
            AutoNomenSuggestion autoNomenSuggestion = new AutoNomenSuggestion();
            autoNomenSuggestion.setInputName(inputName);
            autoNomenSuggestion.setAuthority(authority);

            return autoNomenSuggestion;
        }
    }

    @Override
    public String toString() {
        return "AutoNomenSuggestion{" +
                "inputName='" + inputName + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }
}
