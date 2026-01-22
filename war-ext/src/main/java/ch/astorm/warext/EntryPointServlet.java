
package ch.astorm.warext;

import ch.astorm.api.SimpleBeanRemote;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.naming.InitialContext;

@WebServlet(urlPatterns = {"/"})
public class EntryPointServlet extends HttpServlet {

    //@EJB
    private static SimpleBeanRemote remoteBean;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        
        if(remoteBean==null) {
            try {
                InitialContext ic = new InitialContext();
                remoteBean = (SimpleBeanRemote)ic.lookup(SimpleBeanRemote.class.getName());
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        req.logout();
        req.login(user, password);
        if(!List.of("USER", "ADMIN").stream().anyMatch(s -> req.isUserInRole(s))) { throw new RuntimeException("No role"); }
        req.logout();
        req.login(user, password);
        if(!List.of("USER", "ADMIN").stream().anyMatch(s -> req.isUserInRole(s))) { throw new RuntimeException("No role"); }
        req.logout();
        req.login(user, password);
        if(!List.of("USER", "ADMIN").stream().anyMatch(s -> req.isUserInRole(s))) { throw new RuntimeException("No role"); }
        
        StringBuilder builder = new StringBuilder();
        for(String role : List.of("USER", "ADMIN")) {
            boolean hasRole = req.isUserInRole(role);
            if(builder.length()>0) { builder.append("<br/>"); }
            if(hasRole) { builder.append("User '").append(user).append("' has the role ").append(role); }
            else { builder.append("User '").append(user).append("' has NOT the role ").append(role); }
        }
        
        String query = req.getParameter("query");
        if(query!=null) {
            builder.append("<br/>").append(remoteBean.getLeaf(0));
            builder.append("<br/>").append(remoteBean.getLeaf(0));
        }
        
        req.logout();
    }
}
