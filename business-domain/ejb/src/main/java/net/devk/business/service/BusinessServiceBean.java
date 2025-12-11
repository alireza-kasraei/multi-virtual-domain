package net.devk.business.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import net.devk.echo.service.EchoService;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

import java.util.UUID;

@Stateless
public class BusinessServiceBean implements BusinessService {

    @EJB(lookup = "java:global/echo/echo-ejb/EchoServiceBean!net.devk.echo.service.EchoService")
    private EchoService echoService;

    @Override
    public String getRandomMessage() {
        SecurityDomain current = SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = current.getCurrentSecurityIdentity();
        System.out.println("**************** Current security identity: " + currentSecurityIdentity.toString());
        for (String role : currentSecurityIdentity.getRoles()) {
            System.out.println("Role: " + role);
        }


        return echoService.echo(UUID.randomUUID().toString());
    }
}
