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

@WebServlet(urlPatterns = {"/"})
public class EntryPointServlet extends HttpServlet {

    @EJB
    private SimpleBean bean;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        String queryStr = req.getParameter("query");
        if(queryStr!=null) {
            result = bean.getLeaf(Long.parseLong(queryStr));
        } else {
            result = ""+bean.create()+" leafs created";
        }
        
        String page = "<html><head></head><body>"+result+"</body></html>";
        
        byte[] responseBytes = page.getBytes(StandardCharsets.UTF_8);
        resp.setContentLength(responseBytes.length);
        try(OutputStream os = resp.getOutputStream()) {
            os.write(responseBytes);
        }
    }
}
