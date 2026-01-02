package net.devk.client;

import net.devk.business.common.SecuredService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Main {

    private static String JNDI_NAME = "ejb:business-domain/secured-ejb/SecuredServiceBean!net.devk.business.common.SecuredService";
    private static String DEFAULT_TOKEN_URL = "http://localhost:8081/realms/myrealm/protocol/openid-connect/token";
    private static String CLIENT_ID = "myclient";
    private static String CLIENT_SECRET = "Xyedh9LkftLskTdwNow01IGlYpWXmUGo";
    private static String USERNAME = "user1";
    private static String PASSWORD = "user1";


    public static void main(String[] args) throws Exception {
        AccessTokenHelper.getToken(DEFAULT_TOKEN_URL, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD)
                .thenAccept(Main::executeWithToken).get();
    }

    public static void executeWithToken(String accessToken) {
        SecuredService securedService = lookup(SecuredService.class, JNDI_NAME);
        System.out.println("caller info = " + AuthenticatedContext.execute(() -> securedService.getCallerInfo(), accessToken));
        System.out.println("message from admin = " + AuthenticatedContext.execute(() -> securedService.someAdminOperation(), accessToken));
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