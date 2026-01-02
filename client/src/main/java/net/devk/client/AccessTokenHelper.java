package net.devk.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

import com.fasterxml.jackson.core.type.TypeReference;

public class AccessTokenHelper {

    private static UncheckedObjectMapper mapper = new UncheckedObjectMapper();

    public static CompletableFuture<String> getToken(String tokenUrl, String clientId, String clientSecret, String username, String password) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(tokenUrl));
        HttpRequest httpRequest = builder.header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(String.format("grant_type=password&client_id=%s&client_secret=%s&username=%s&password=%s", clientId, clientSecret, username, password)))
                .build();
        return HttpClient
                    .newHttpClient()
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply((Function<HttpResponse<String>, Object>) HttpResponse::body)
                    .thenApply(Object::toString)
                    .thenApply(s -> mapper.readValue(s))
                    .thenApply(stringObjectMap -> stringObjectMap.get("access_token"))
                    .thenApply(Object::toString);
    }

    static class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {

        /**
         * Parses the given JSON string into a Map.
         */
        Map<String, Object> readValue(String content) {
            try {
                return this.readValue(content, new TypeReference<Map<String, Object>>() {
                });
            } catch (IOException ioe) {
                throw new CompletionException(ioe);
            }
        }

    }

}
