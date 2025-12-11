package net.devk.client;

import net.devk.business.service.MySecurityDomainBusinessService;
import org.wildfly.naming.client.WildFlyInitialContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext(initializeProperties());

        MySecurityDomainBusinessService mySecurityDomainBusinessService = (MySecurityDomainBusinessService) initialContext.lookup("java:global/business-domain/ejb-another-security-domain/MySecurityDomainBusinessServiceBean!net.devk.business.service.MySecurityDomainBusinessService");

        String message = AuthenticatedContext.execute(() -> mySecurityDomainBusinessService.getMessage());
        System.out.println("message = " + message);


    }

    private static Hashtable<String, String> initializeProperties() {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.PROVIDER_URL, "http://localhost:8080");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        jndiProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS",
                "false");
        jndiProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEX",
                "false");
        jndiProperties.put("jboss.naming.client.ejb.context", "true");
        return jndiProperties;
    }
}