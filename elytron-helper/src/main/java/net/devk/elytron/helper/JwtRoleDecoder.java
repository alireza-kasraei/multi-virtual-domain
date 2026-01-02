package net.devk.elytron.helper;

import org.wildfly.security.authz.AuthorizationIdentity;
import org.wildfly.security.authz.RoleDecoder;
import org.wildfly.security.authz.Roles;

import java.util.Set;

public class JwtRoleDecoder implements RoleDecoder {
    @Override
    public Roles decodeRoles(AuthorizationIdentity authorizationIdentity) {
        return Roles.fromSet(Set.of("admin", "user"));
    }
}
