package net.devk.business.service.calculator;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import net.devk.business.common.EchoService;
import org.jboss.logging.Logger;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

@Stateless
public class CalculatorServiceBean implements CalculatorService {

    private static final Logger logger = Logger.getLogger(CalculatorServiceBean.class);

    @EJB(lookup = "java:global/business-domain/echo-ejb/EchoServiceBean!net.devk.business.common.EchoService")
    private EchoService echoService;


    @Override
    public int add(int a, int b) {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        String name = currentSecurityIdentity.getPrincipal().getName();
        logger.info(String.format("+++ Current security identity: %s", name));
        for (String role : currentSecurityIdentity.getRoles()) {
            logger.info("Role: " + role);
        }
        echoService.echo(name + " says...");
        return a + b;
    }
}
