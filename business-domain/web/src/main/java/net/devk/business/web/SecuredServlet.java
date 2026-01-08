package net.devk.business.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.devk.business.service.locale.LocaleServiceBean;
import net.devk.business.service.users.AdminServiceBean;
import net.devk.business.service.users.UserServiceBean;

@WebServlet("/secured")
public class SecuredServlet extends HttpServlet {

    @EJB(lookup = "java:global/business-domain/business-ejb/UserServiceBean")
    private UserServiceBean userService;

    @EJB(lookup = "java:global/business-domain/business-ejb/AdminServiceBean")
    private AdminServiceBean adminService;

    @EJB(lookup = "java:global/business-domain/business-ejb/LocaleServiceBean")
    private LocaleServiceBean localeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final PrintWriter writer = resp.getWriter();

        writer.println("<html><head><title>Elytron OIDC + EJB Remote call</title></head><body>");
        writer.println("<h1>Successfully logged into Secured Servlet with OIDC</h1>");

        writer.println("<h2>Identity as visible to servlet.</h2>");
        writer.println(String.format("<p>Principal  : %s</p>", req.getUserPrincipal().getName()));
        writer.println(String.format("<p>Authentication Type : %s</p>", req.getAuthType()));
        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "User", req.isUserInRole("User")));
        boolean isAdmin = req.isUserInRole("Admin");
        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "Admin", isAdmin));
        writer.println("<hr/>");

        writer.println("<h2>EJB Calls:</h2>");
        writer.println(
                String.format("<p>Current user info from UserServiceBean : %s</p>", userService.getCurrentUserInfo()));
        if (isAdmin) {
            writer.println(String.format("<p>Message from Admin : %s</p>", adminService.getMessageFromAdmin()));
        }
        writer.println("<hr/>");
        writer.println("<h3>System Locales</h3>");
        String[] systemLanguages = localeService.getSystemLanguages();
        writer.println("<ul>");
        for (String systemLanguage : systemLanguages) {
            writer.println(String.format("<li>%s</li>", systemLanguage));
        }
        writer.println("</ul>");
        writer.println("</body></html>");
        writer.close();
    }

}
