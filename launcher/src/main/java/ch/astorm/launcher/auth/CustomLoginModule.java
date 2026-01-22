
package ch.astorm.launcher.auth;

import com.sun.enterprise.security.BasePasswordLoginModule;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.Realm;
import com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm;
import jakarta.security.jacc.PolicyContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

public class CustomLoginModule extends BasePasswordLoginModule {
    private static final Logger LOG = Logger.getLogger(CustomLoginModule.class.getName());
    private static final ThreadLocal<String> THREADLOCAL_CONTEXTID = new ThreadLocal<>();
    
    @Override
    protected void authenticateUser() throws LoginException {
        String user = getUsername();
        char[] password = getPasswordChar();

        //prevents login with an empty password
        //it seems that an LDAP server can be configured to accept anonymous authentication
        //of a user without password: https://stackoverflow.com/questions/27873362/ldap-bind-seems-to-return-true-with-blank-password
        if(password.length<=0) { throw new LoginException("No password provided"); }

        //HACK
        //the context id is set to null when the logout is called and not set back when
        //a further login is done
        String policyContextId = PolicyContext.getContextID();
        if(policyContextId==null) {
            String contextId = THREADLOCAL_CONTEXTID.get();
            if(contextId!=null) { PolicyContext.setContextID(contextId); }
        } else {
            THREADLOCAL_CONTEXTID.set(policyContextId);
        }
        
        CustomRealm realm;
        try {
            realm = (CustomRealm)Realm.getInstance("customRealm");
        } catch(NoSuchRealmException nsr) {
            LOG.log(Level.SEVERE, "Unable to find customRealm", nsr);
            throw new LoginException("Unable to find customRealm");
        }
        
        String realmName = null;
        String[] groups = realm.authenticate(user, password);
        if(groups==null) {
            realmName = realm.getProperty("realm-name");
            if(realmName==null || realmName.isEmpty()) {
                LOG.log(Level.SEVERE, "Property realm-name not defined");
                throw new LoginException("Property realm-name not defined");
            }
            
            try {
                Realm fallbackRealm = Realm.getInstance(realmName);
                if(fallbackRealm instanceof JDBCRealm jdbc) {
                    groups = jdbc.authenticate(user, password);
                } else {
                    LOG.log(Level.SEVERE, "Unhandled realm class {0} for realm {1}", new Object[]{fallbackRealm.getClass().getName(), realmName});
                    throw new LoginException("Unhandled realm");
                }
            } catch(NoSuchRealmException nre) {
                LOG.log(Level.SEVERE, "Unable to lookup realm {0}", realmName);
                LOG.log(Level.SEVERE, "The realm does not exist", nre);
                throw new LoginException("Invalid realm configuration");
            } catch(LoginException e) {
                LOG.log(Level.SEVERE, "Unable to login with {0}", user);
                throw e;
            }
        }

        if(groups==null || groups.length==0) {
            LOG.log(Level.SEVERE, "User {0} has no group (realm: {1})", new Object[]{user, realmName});
            throw new LoginException("User "+user+" has no group");
        }
        
        commitUserAuthentication(groups);
    }
}
