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
    public String getCallerInfo() {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        StringBuilder rolesBuilder = new StringBuilder();
        currentSecurityIdentity.getRoles().forEach(role -> rolesBuilder.append(role).append(", "));
        return String.format("Security identity = %s%nRoles: %s", currentSecurityIdentity, rolesBuilder);
    }

    @RolesAllowed("admin")
    @Override
    public String someAdminOperation() {
        return "Admin says hello!";
    }
}
