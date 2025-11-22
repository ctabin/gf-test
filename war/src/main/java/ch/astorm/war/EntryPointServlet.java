package ch.astorm.war;


import ch.astorm.ejb.SimpleBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {"/"})
public class EntryPointServlet extends HttpServlet {

    @EJB
    private SimpleBean bean;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        
        req.login(user, password);
        
        StringBuilder builder = new StringBuilder();
        for(String role : List.of("USER", "ADMIN")) {
            boolean hasRole = req.isUserInRole(role);
            if(builder.length()>0) { builder.append("<br/>"); }
            if(hasRole) { builder.append("User '").append(user).append("' has the role ").append(role); }
            else { builder.append("User '").append(user).append("' has NOT the role ").append(role); }
        }
        
        req.logout();
        req.login(user, password);
        
        for(String role : List.of("USER", "ADMIN")) {
            boolean hasRole = req.isUserInRole(role);
            if(builder.length()>0) { builder.append("<br/>"); }
            if(hasRole) { builder.append("User '").append(user).append("' has the role ").append(role); }
            else { builder.append("User '").append(user).append("' has NOT the role ").append(role); }
        }
        
        String page = "<html><head></head><body><p>"+builder+"</p></body></html>";
        
        byte[] responseBytes = page.getBytes(StandardCharsets.UTF_8);
        resp.setContentLength(responseBytes.length);
        try(OutputStream os = resp.getOutputStream()) {
            os.write(responseBytes);
        }
        
        req.logout();
    }
}
