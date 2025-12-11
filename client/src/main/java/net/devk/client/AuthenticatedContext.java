package net.devk.client;

import org.wildfly.security.WildFlyElytronProvider;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.credential.BearerTokenCredential;
import org.wildfly.security.sasl.SaslMechanismSelector;

import java.security.Provider;
import java.util.Map;
import java.util.concurrent.Callable;

public class AuthenticatedContext {

    private static AuthenticationConfiguration configuration;
    private static String token = """
            """;

    static {
        configuration = AuthenticationConfiguration.empty()
                .useSaslMechanismProperties(Map.of("wildfly.sasl.local-user.quiet-auth", false,
                        "javax.security.sasl.policy.noanonymous", true))
                .useProviders(() -> new Provider[]{new WildFlyElytronProvider()}).useRealm("jwt-realm")
                .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("OAUTHBEARER"));
    }

    public static <V> V execute(Callable<V> callable) throws Exception {
        AuthenticationConfiguration authenticationConfiguration = configuration
                .useBearerTokenCredential(new BearerTokenCredential(token));
        return AuthenticationContext.empty().with(MatchRule.ALL, authenticationConfiguration).runCallable(callable);
    }

}