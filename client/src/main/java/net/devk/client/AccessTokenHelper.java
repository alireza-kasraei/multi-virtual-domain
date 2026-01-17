package net.devk.client;

import net.devk.commons.helper.UncheckedObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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


}
