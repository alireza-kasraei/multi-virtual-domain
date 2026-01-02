package net.devk.client;

import org.wildfly.security.WildFlyElytronProvider;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.credential.BearerTokenCredential;
import org.wildfly.security.sasl.SaslMechanismSelector;

import java.security.Provider;
import java.util.Map;
import java.util.function.Supplier;

public class AuthenticatedContext {

    private static AuthenticationConfiguration configuration;


    static {
        configuration = AuthenticationConfiguration.empty()
                .useSaslMechanismProperties(Map.of("wildfly.sasl.local-user.quiet-auth", false,
                        "javax.security.sasl.policy.noanonymous", true))
                .useProviders(() -> new Provider[]{new WildFlyElytronProvider()}).useRealm("jwt-realm")
                .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("OAUTHBEARER"));
    }

    public static <V> V execute(Supplier<V> supplier, String accessToken) {
        AuthenticationConfiguration authenticationConfiguration = configuration
                .useBearerTokenCredential(new BearerTokenCredential(accessToken));
        return AuthenticationContext.empty().with(MatchRule.ALL, authenticationConfiguration).runAsSupplier(supplier);
    }

}