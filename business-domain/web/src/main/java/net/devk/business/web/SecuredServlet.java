package net.devk.business.web;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.devk.business.service.users.AdminServiceBean;
import net.devk.business.service.users.UserServiceBean;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/secured")
public class SecuredServlet extends HttpServlet {

    @EJB(lookup = "java:global/business-domain/business-ejb/UserServiceBean")
    private UserServiceBean userService;

    @EJB(lookup = "java:global/business-domain/business-ejb/AdminServiceBean")
    private AdminServiceBean adminService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final PrintWriter writer = resp.getWriter();

        writer.println("<html><head><title>Elytron OIDC + EJB Remote call</title></head><body>");
        writer.println(String.format("<p id=\"callerInfo\">Caller: %s</p>", userService.getCurrentUserInfo()));
        if (req.isUserInRole("Admin")) {
            writer.println(String.format("<p id=\"adminMessage\">Message from Admin: %s</p>", adminService.getMessageFromAdmin()));
        }
        writer.println("</body></html>");
        writer.close();
    }

}
