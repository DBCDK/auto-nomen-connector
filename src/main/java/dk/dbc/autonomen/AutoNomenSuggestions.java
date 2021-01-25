/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.autonomen;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class AutoNomenSuggestions {

    @JsonProperty("aut-names")
    @JacksonXmlProperty(localName = "aut-name")
    @JacksonXmlElementWrapper(localName = "aut-names")
    private List<AutoNomenSuggestion> autNames;

    @JsonProperty("ner-names")
    @JacksonXmlProperty(localName = "ner-name")
    @JacksonXmlElementWrapper(localName = "ner-names")
    private List<AutoNomenSuggestion> nerNames;

    @JsonProperty("exact-match-names")
    @JacksonXmlProperty(localName = "exact-match-name")
    @JacksonXmlElementWrapper(localName = "exact-match-names")
    private List<ExactMatchNames> exactMatchNames;

    public List<AutoNomenSuggestion> getAutNames() {
        return autNames;
    }

    public void setAutNames(List<AutoNomenSuggestion> autNames) {
        this.autNames = autNames;
    }

    public List<AutoNomenSuggestion> getNerNames() {
        return nerNames;
    }

    public void setNerNames(List<AutoNomenSuggestion> nerNames) {
        this.nerNames = nerNames;
    }

    public List<ExactMatchNames> getExactMatchNames() {
        return exactMatchNames;
    }

    public void setExactMatchNames(List<ExactMatchNames> exactMatchNames) {
        this.exactMatchNames = exactMatchNames;
    }

    @Override
    public String toString() {
        return "AuthorNameSuggestions{" +
                "autNames=" + autNames +
                ", nerNames=" + nerNames +
                ", exactMatchNames=" + exactMatchNames +
                '}';
    }
}
