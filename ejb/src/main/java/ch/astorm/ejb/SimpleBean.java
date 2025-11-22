
package ch.astorm.ejb;

import ch.astorm.api.SimpleBeanRemote;
import ch.astorm.ejb.entities.User;
import ch.astorm.ejb.entities.UserCategory;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Remote;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Startup
@Singleton
@LocalBean
@Remote(SimpleBeanRemote.class)
public class SimpleBean {

    @PersistenceContext(unitName="sample-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        String password = "changeit";
        
        MessageDigest digest;
        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch(Exception e) { throw new RuntimeException(e); }
        
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encodedPassword = new String(Base64.getEncoder().encode(encodedhash), StandardCharsets.UTF_8);
        
        User admin = new User();
        admin.setUserpk("admin");
        admin.setPassword(encodedPassword);
        admin.setName("Administrator");
        em.persist(admin);
        
        UserCategory adminCategory1 = new UserCategory();
        adminCategory1.setId(1L);
        adminCategory1.setRoleid("ADMIN");
        adminCategory1.setUserpk(admin.getUserpk());
        em.persist(adminCategory1);
        
        UserCategory adminCategory2 = new UserCategory();
        adminCategory2.setId(2L);
        adminCategory2.setRoleid("USER");
        adminCategory2.setUserpk(admin.getUserpk());
        em.persist(adminCategory2);
        
        User user = new User();
        user.setUserpk("user");
        user.setPassword(encodedPassword);
        user.setName("User");
        em.persist(user);
        
        UserCategory userCategory = new UserCategory();
        userCategory.setId(3L);
        userCategory.setRoleid("USER");
        userCategory.setUserpk(user.getUserpk());
        em.persist(userCategory);
        
        System.out.println("### [INIT] EJB initialized");
    }
    
    public String getLeaf(long id) {
        return "empty";
    }
}
