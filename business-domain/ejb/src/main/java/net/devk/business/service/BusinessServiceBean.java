package net.devk.business.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

@Stateless
public class BusinessServiceBean implements BusinessService {

    @EJB
    private MySecurityDomainBusinessService mySecurityDomainBusinessService;

    @Override
    public String getRandomMessage() {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        System.out.println("**************** Current security identity: " + currentSecurityIdentity.toString());
        for (String role : currentSecurityIdentity.getRoles()) {
            System.out.println("Role: " + role);
        }
        return mySecurityDomainBusinessService.getMessage();
    }
}
