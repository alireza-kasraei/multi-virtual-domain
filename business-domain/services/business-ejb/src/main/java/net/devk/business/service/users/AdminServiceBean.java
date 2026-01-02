package net.devk.business.service.users;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import net.devk.business.service.security.SecuredServiceBean;

@Stateless
public class AdminServiceBean implements AdminService {

    @EJB(lookup = "java:global/business-domain/secured-ejb/SecuredServiceBean!net.devk.business.common.SecuredService")
    private SecuredServiceBean securedServiceBean;


    @Override
    public String getMessageFromAdmin() {
        return securedServiceBean.someAdminOperation();
    }
}
