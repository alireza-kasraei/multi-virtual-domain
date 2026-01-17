package net.devk.commons.helper;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletionException;

public class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {

    /**
     * Parses the given JSON string into a Map.
     */
    public Map<String, Object> readValue(String content) {
        try {
            return this.readValue(content, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException ioe) {
            throw new CompletionException(ioe);
        }
    }

    public Keys readKeys(String content) {
        try {
            return this.readValue(content, new TypeReference<Keys>() {
            });
        } catch (IOException ioe) {
            throw new CompletionException(ioe);
        }
    }


}
