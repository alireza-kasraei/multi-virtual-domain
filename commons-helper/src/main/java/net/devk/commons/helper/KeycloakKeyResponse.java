package net.devk.commons.helper;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakKeyResponse(String kid,
                                  String kty,
                                  String alg,
                                  String use,
                                  String n,
                                  String e,
                                  String x5t,
                                  @JsonProperty("x5t#S256") String x5tS256,
                                  String[] x5c) {
}
