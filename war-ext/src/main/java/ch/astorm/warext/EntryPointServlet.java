
package ch.astorm.warext;

import ch.astorm.api.SimpleBeanRemote;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = {"/"})
public class EntryPointServlet extends HttpServlet {

    @EJB
    private SimpleBeanRemote remoteBean;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        String queryStr = req.getParameter("query");
        if(queryStr!=null) {
            result = remoteBean.getLeaf(Long.parseLong(queryStr));
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        String page = "<html><head></head><body>"+result+"</body></html>";
        
        byte[] responseBytes = page.getBytes(StandardCharsets.UTF_8);
        resp.setContentLength(responseBytes.length);
        try(OutputStream os = resp.getOutputStream()) {
            os.write(responseBytes);
        }
    }
}
