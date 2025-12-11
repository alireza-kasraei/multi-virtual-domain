package net.devk.business.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.devk.business.service.BusinessService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/secured")
public class SecuredServlet extends HttpServlet {

    @EJB(lookup = "java:global/business-domain/business-ejb/BusinessServiceBean!net.devk.business.service.BusinessService")
    private BusinessService businessService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        BusinessService bean = lookup(BusinessService.class, "java:global/virtual-security-domain-to-domain/virtual-security-domain-to-domain-ejb/EntryBean");
        final PrintWriter writer = resp.getWriter();

        writer.println("<html><head><title>virtual-security-domain-to-domain</title></head><body>");
        writer.println("<h1>Successfully logged into Secured Servlet with OIDC</h1>");
        writer.println("<h2>Identity as visible to servlet.</h2>");
        writer.println(String.format("<p>Principal  : %s</p>", req.getUserPrincipal().getName()));
        writer.println(String.format("<p>Authentication Type : %s</p>", req.getAuthType()));

        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "User", req.isUserInRole("User")));
        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "Admin", req.isUserInRole("Admin")));

        writer.println("<h2>Identity as visible to EntryBean.</h2>");

        writer.println(String.format("<p>got random message : %s </p>", businessService.getRandomMessage()));


        writer.println("</body></html>");
        writer.close();
    }

    public static <T> T lookup(Class<T> clazz, String jndiName) {
        Object bean = lookup(jndiName);
        return clazz.cast(bean);
    }

    private static Object lookup(String jndiName) {
        Context context = null;
        try {
            context = new InitialContext();
            return context.lookup(jndiName);
        } catch (NamingException ex) {
            throw new IllegalStateException("Lookup failed", ex);
        } finally {
            try {
                context.close();
            } catch (NamingException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
}
