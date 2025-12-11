/*
 * Copyright 2023 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.devk.echo.service;

import jakarta.annotation.Resource;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

@Stateful
@Remote(EchoService.class)
@SecurityDomain("BusinessDomain")
public class EchoServiceBean implements EchoService {

    @Resource
    private SessionContext sessionContext;

    @Override
//    @RolesAllowed("Admin")
    public String echo(String message) {

        org.wildfly.security.auth.server.SecurityDomain securityDomain = org.wildfly.security.auth.server.SecurityDomain.getCurrent();
        SecurityIdentity currentSecurityIdentity = securityDomain.getCurrentSecurityIdentity();
        System.out.println("**************** ECHO Current security identity: " + currentSecurityIdentity.toString());
        for (String role : currentSecurityIdentity.getRoles()) {
            System.out.println("Role: " + role);
        }


        String callerPrincipal = sessionContext.getCallerPrincipal().getName();
//        boolean isCallerUser = sessionContext.isCallerInRole("User");
//        boolean isCallerAdmin = sessionContext.isCallerInRole("Admin");
        return String.format("Principal : %s , message: %s", callerPrincipal, message);
    }
}
