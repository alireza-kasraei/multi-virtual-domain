package net.devk.elytron.helper;

import java.util.Set;

import org.wildfly.security.authz.AuthorizationIdentity;
import org.wildfly.security.authz.RoleDecoder;
import org.wildfly.security.authz.Roles;

public class JwtRoleDecoder implements RoleDecoder {
    @Override
    public Roles decodeRoles(AuthorizationIdentity authorizationIdentity) {
        return Roles.fromSet(Set.of("admin", "user"));
    }
}
