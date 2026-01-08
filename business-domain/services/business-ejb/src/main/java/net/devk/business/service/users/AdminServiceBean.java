package net.devk.business.service.users;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import net.devk.business.service.security.SecuredServiceBean;

@Stateless
public class AdminServiceBean {

    @EJB(lookup = "java:global/business-domain/secured-ejb/SecuredServiceBean!net.devk.business.common.SecuredService")
    private SecuredServiceBean securedServiceBean;


    public String getMessageFromAdmin() {
        return securedServiceBean.someAdminOperation();
    }
}
