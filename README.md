### wildfly 36

to run the keycloak with docker, run this command from the root directory of the project:

```
docker run --name keycloak -p 8081:8080 \ 
        -e KC_BOOTSTRAP_ADMIN_USERNAME=keycloak-admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
        -v ./keycloak/data:/opt/keycloak/data/import \
        quay.io/keycloak/keycloak:latest \
        start-dev --import-realm
```

This creates an instance of the keycloak and imports the "myrealm" to it.
2 additional manual steps are needed here which might be automated in the future. first, set a password for the user1,
then copy the "kid" and the "public key" from the "keys" section of the myrealm and paste them in the realm.properties file.

now build the project simply with:

```
mvn clean install
```
now from "multi-virtual-domain/business-domain/business-ear":

start wildfly with: ```mvn wildfly:start```

execute cli commands: ```mvn wildfly:execute-commands```

deploy the application: ```mvn wildfly:deploy```

the web application should be available on "http://localhost:8080/business".
the secured page is on "http://localhost:8080/business/secured" address and it redirects to the keycloak for the authentication.