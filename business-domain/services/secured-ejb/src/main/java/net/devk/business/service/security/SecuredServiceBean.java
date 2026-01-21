package net.devk.business.service.security;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import net.devk.business.common.SecuredService;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

@org.jboss.ejb3.annotation.SecurityDomain("myAppDomain")
@Remote(SecuredService.class)
@Stateless
public class SecuredServiceBean implements SecuredService {

    @Override
    public String getCaller() {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        return currentSecurityIdentity.getPrincipal().getName();
    }

    @RolesAllowed("Admin")
    @Override
    public String someAdminOperation() {
        return "Admin says hello!";
    }
}
