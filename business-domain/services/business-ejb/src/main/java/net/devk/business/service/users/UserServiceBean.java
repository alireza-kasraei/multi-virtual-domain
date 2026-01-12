package net.devk.business.service.users;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import net.devk.business.common.SecuredService;

@Stateless
public class UserServiceBean {

    @EJB(lookup = "java:global/business-domain/secured-ejb/SecuredServiceBean!net.devk.business.common.SecuredService")
    private SecuredService securedService;


    public String getCurrentUserInfo() {
        return securedService.getCallerInfo();
    }
}
