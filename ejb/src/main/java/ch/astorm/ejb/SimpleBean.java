
package ch.astorm.ejb;

import ch.astorm.api.SimpleBeanRemote;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;

@Stateless
@LocalBean
@Remote(SimpleBeanRemote.class)
@RolesAllowed({"ADMIN", "USER"})
@TransactionManagement(TransactionManagementType.BEAN)
public class SimpleBean implements SimpleBeanRemote {
    @PersistenceContext(unitName="sample-ejbPU")
    private EntityManager em;
    
    @Resource
    private UserTransaction utx;
    
    @Resource
    private SessionContext sctx;
    
    @Override
    public String getLeaf(long id) {
        try {
            utx.begin();
            utx.commit();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return "id: "+id;
    }
}
