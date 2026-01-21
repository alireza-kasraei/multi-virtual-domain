package net.devk.client;

import net.devk.business.common.SecuredService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class EjbRemoteIT {


    private static String JNDI_NAME = "ejb:business-domain/secured-ejb/SecuredServiceBean!net.devk.business.common.SecuredService";
    private static String DEFAULT_TOKEN_URL = "http://localhost:8081/realms/myrealm/protocol/openid-connect/token";
    private static String CLIENT_ID = "myclient";
    private static String CLIENT_SECRET = "Xyedh9LkftLskTdwNow01IGlYpWXmUGo";
    private static String USERNAME = "user1";
    private static String PASSWORD = "u";

    private static String accessToken;
    private static SecuredService securedService;

    @BeforeAll
    public static void init() throws Exception {
        accessToken = AccessTokenHelper.getToken(DEFAULT_TOKEN_URL, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD).get();
        securedService = lookup(SecuredService.class, JNDI_NAME);
    }


    @Test
    void callerHasToBeUser1() {
        Assertions.assertEquals("user1", AuthenticatedContext.execute(() -> securedService.getCaller(), accessToken));
    }

    @Test
    void testExpectedMessage() {
        Assertions.assertEquals("Admin says hello!", AuthenticatedContext.execute(() -> securedService.someAdminOperation(), accessToken));
    }


    public static <T> T lookup(Class<T> clazz, String jndiName) {
        Object bean = lookup(jndiName);
        return clazz.cast(bean);
    }

    private static Object lookup(String jndiName) {
        Context context = null;
        try {
            context = new InitialContext(initializeProperties());
            return context.lookup(jndiName);
        } catch (NamingException ex) {
            throw new IllegalStateException("Lookup failed ", ex);
        } finally {
            try {
                context.close();
            } catch (NamingException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    private static Hashtable<String, String> initializeProperties() {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put("jboss.naming.client.ejb.context", "true");
        return jndiProperties;
    }
}
