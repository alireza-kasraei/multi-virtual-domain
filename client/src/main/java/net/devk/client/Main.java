package net.devk.client;

import net.devk.business.common.EchoService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {

        String jndiName = "ejb:business-domain/echo-ejb/EchoServiceBean!net.devk.business.common.EchoService";

        EchoService echoService = lookup(EchoService.class, jndiName);

        String message = AuthenticatedContext.execute(() -> echoService.echo("Hello World!"));
        System.out.println("message = " + message);

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
//        jndiProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS",
//                "false");
//        jndiProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEX",
//                "false");
        jndiProperties.put("jboss.naming.client.ejb.context", "true");
        return jndiProperties;
    }
}