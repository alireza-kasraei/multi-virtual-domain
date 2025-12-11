package net.devk.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.wildfly.security.WildFlyElytronProvider;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.credential.BearerTokenCredential;
import org.wildfly.security.sasl.SaslMechanismSelector;

import java.io.IOException;
import java.security.Provider;
import java.util.Map;
import java.util.concurrent.Callable;

public class AuthenticatedContext {

    private static AuthenticationConfiguration configuration;
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        configuration = AuthenticationConfiguration.empty()
                .useSaslMechanismProperties(Map.of("wildfly.sasl.local-user.quiet-auth", false,
                        "javax.security.sasl.policy.noanonymous", true))
                .useProviders(() -> new Provider[]{new WildFlyElytronProvider()}).useRealm("jwt-realm")
                .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("OAUTHBEARER"));
    }

    public static <V> V execute(Callable<V> callable) throws Exception {
        String newToken = getToken();
        AuthenticationConfiguration authenticationConfiguration = configuration
                .useBearerTokenCredential(new BearerTokenCredential(newToken));
        return AuthenticationContext.empty().with(MatchRule.ALL, authenticationConfiguration).runCallable(callable);
    }

    private static String getToken() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=password&client_id=myclient&client_secret=1RevGaBShLOSHecZeVmvvcm4TF5m9Cd4&username=ali&password=a");
        Request request = new Request.Builder()
                .url("http://localhost:8081/realms/myrealm/protocol/openid-connect/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        Map<String, Object> responseMap = mapper.readValue(response.body().string(), new TypeReference<Map<String, Object>>() {
        });
        return responseMap.get("access_token").toString();
    }

}