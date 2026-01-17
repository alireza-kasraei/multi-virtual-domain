package net.devk.mojo;

import net.devk.commons.helper.Keys;
import net.devk.commons.helper.UncheckedObjectMapper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Properties;

import static java.nio.file.StandardOpenOption.CREATE;

@Mojo(name = "keycloak-publickey-resolver")
public class KeycloakPublicKeyResolverMojo extends AbstractMojo {

    public static final String KEYCLOAK_CERTS_URL = "http://localhost:8081/realms/myrealm/protocol/openid-connect/certs";
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    private static UncheckedObjectMapper mapper = new UncheckedObjectMapper();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenProject rootProject = project;
        while (rootProject.getParent() != null) {
            getLog().info("Parent project found : " + rootProject.getParent().getName());
            rootProject = rootProject.getParent();
        }
        Path keycloakPath = rootProject.getBasedir().toPath().resolve("keycloak");
        getLog().info(String.format("getting certificates from %s ...", KEYCLOAK_CERTS_URL));
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(KEYCLOAK_CERTS_URL));
        HttpRequest httpRequest = builder
                .GET()
                .build();
        try {
            Properties properties = new Properties();
            HttpResponse<String> stringHttpResponse = HttpClient
                    .newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = stringHttpResponse.body();
            getLog().info(String.format("body: %s", body));
            Keys keys = mapper.readKeys(body);
            keys.keys()
                    .stream()
                    .filter(k -> k.alg().equals("RS256"))
                    .findAny().ifPresent(key -> {
                        properties.put("kid", key.kid());
                        properties.put("key", extractPublicKey(key.x5c()[0]));
                    });
            Files.createDirectories(keycloakPath);
            getLog().info("storing realm.properties ...");
            OutputStream outputStream = Files.newOutputStream(keycloakPath.resolve("realm.properties"), CREATE);
            properties.store(outputStream, null);
        } catch (IOException | InterruptedException e) {
            getLog().error(e.getMessage());
        }
    }

    String extractPublicKey(String x5c) {
        byte[] derBytes = Base64.getDecoder().decode(x5c);

        try {
            // Generate X509Certificate object
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(
                    new ByteArrayInputStream(derBytes));

            return String.format("-----BEGIN PUBLIC KEY-----%s-----END PUBLIC KEY-----",
                    Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
        } catch (CertificateException e) {
            getLog().warn(e.getMessage());
        }
        return null;
    }


}
