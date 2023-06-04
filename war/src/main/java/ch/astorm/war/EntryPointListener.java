
package ch.astorm.war;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class EntryPointListener implements ServletContextListener {
    private static int counter;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("### [INIT] WAR initialized");
        
        ++counter;
        if(System.getProperty("test-context")!=null && counter>1) {
            throw new IllegalStateException("Already initialized");
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        --counter;
        System.out.println("### [CLOSE] WAR destroyed");
    }
}
