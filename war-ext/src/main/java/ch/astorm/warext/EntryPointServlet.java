
package ch.astorm.warext;

import ch.astorm.api.SimpleBeanRemote;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/"})
public class EntryPointServlet extends HttpServlet {

    @EJB
    private SimpleBeanRemote remoteBean;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        
        req.login(user, password);
        
        if(!req.isUserInRole("USER")) { throw new ServletException("Role USER should be true (before remote EJB call)"); }
        remoteBean.getLeaf(0);
        if(!req.isUserInRole("USER")) { throw new ServletException("Role USER should be true (after remote EJB call)"); }
        
        req.logout();
    }
}
