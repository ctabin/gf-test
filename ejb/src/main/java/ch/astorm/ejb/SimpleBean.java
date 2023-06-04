
package ch.astorm.ejb;

import ch.astorm.api.SimpleBeanRemote;
import ch.astorm.ejb.entities.Leaf;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@LocalBean
@Stateless
@Remote(SimpleBeanRemote.class)
public class SimpleBean {

    @PersistenceContext(unitName="sample-ejbPU")
    private EntityManager em;
    
    public int create() {
        for(int i=0 ; i<10 ; ++i) {
            Leaf leaf = new Leaf();
            leaf.setId((long)i);
            leaf.setName("Leaf "+(i+1));
            em.persist(leaf);
        }
        return 10;
    }
    
    public String getLeaf(long id) {
        return em.createQuery("SELECT l.name FROM Leaf l WHERE l.id=?1", String.class).
            setParameter(1, id).
            getSingleResult();
    }
}
