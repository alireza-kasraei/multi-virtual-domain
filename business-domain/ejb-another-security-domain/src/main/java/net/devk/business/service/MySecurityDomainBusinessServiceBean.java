package net.devk.business.service;

import jakarta.ejb.Stateless;
import org.jboss.logging.Logger;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

@org.jboss.ejb3.annotation.SecurityDomain("MySecurityDomain")
@Stateless
public class MySecurityDomainBusinessServiceBean implements MySecurityDomainBusinessService {

    private static Logger logger = Logger.getLogger(MySecurityDomainBusinessServiceBean.class.getName());

    @Override
    public String getMessage() {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        String message = String.format("Current security identity: %s", currentSecurityIdentity);
        logger.info("**************** " + message);
        for (String role : currentSecurityIdentity.getRoles()) {
            logger.info("Role: " + role);
        }
        return String.format("message from MySecurityDomain %s", message);
    }
}
