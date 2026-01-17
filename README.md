# Wildfly Elytron OIDC Security Identity Propagation Example

This project demonstrates how to propagate security identity between WAR and EJB deployments in Wildfly using Elytron OIDC, with a custom security setup.

## Prerequisites

- Java
- Maven (or use the Maven Wrapper)
- Docker

The default wildfly elytron OIDC, does not propagate the security identity from a war deployment to 
a separate ejb deployment. This example shows how to achieve that with the help of a custom security setup.

to run the keycloak with docker, run this command from the root directory of the project:

### keycloak setup

```zsh
docker run --name keycloak -p 8081:8080 \ 
        -e KC_BOOTSTRAP_ADMIN_USERNAME=keycloak-admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
        -v ./keycloak/data:/opt/keycloak/data/import \
        quay.io/keycloak/keycloak:latest \
        start-dev --import-realm
```

This creates an instance of the keycloak and imports the "myrealm" to it.

now from the root directory, let's build the project simply with: 
```sh
./mvnw clean install
```
a custom maven plugin <b>keycloak-helper</b> creates the realm.properties files which is needed for setting up the wildfly.

start wildfly with: ```./mvnw wildfly:start -pl business-domain/ear```

deploy our custom wildfly module: ```./mvnw wildfly:execute-commands -pl elytron-helper```

execute wildfly configuration script: ```./mvnw wildfly:execute-commands -pl business-domain/ear```

and finally deploy the application: ```./mvnw wildfly:deploy -pl business-domain/ear```

the web application should be available on "http://localhost:8080/business".
the secured page is on "http://localhost:8080/business/secured" address and it redirects to the keycloak for the authentication.
login with "user1" user with "user1" password and you should see the message from the EJB with the propagated security identity.

besides that, a java client application can be run which calls the remote ejb directly.
to run the client, execute: ```java -jar ./client/target/client.jar```

the wildfly server can be shutdown with: ```./mvnw wildfly:shutdown -pl business-domain/ear```