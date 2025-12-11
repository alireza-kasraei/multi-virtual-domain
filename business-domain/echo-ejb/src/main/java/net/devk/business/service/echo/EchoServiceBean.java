package net.devk.business.service.echo;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import net.devk.business.common.EchoService;
import org.jboss.logging.Logger;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

@org.jboss.ejb3.annotation.SecurityDomain("other")
@Remote(EchoService.class)
@Stateless
public class EchoServiceBean implements EchoService {

    private static Logger logger = Logger.getLogger(EchoServiceBean.class.getName());

    @Override
    public String echo(String message) {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        logger.info(String.format("*** Current security identity: %s", currentSecurityIdentity.getPrincipal().getName()));
        for (String role : currentSecurityIdentity.getRoles()) {
            logger.info("Role: " + role);
        }
        return String.format("Echo %s , Principle: %s", message, currentSecurityIdentity.getPrincipal().getName());
    }
}
