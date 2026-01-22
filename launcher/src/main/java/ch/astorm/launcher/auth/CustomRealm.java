
package ch.astorm.launcher.auth;

import com.sun.appserv.connectors.internal.api.ConnectorRuntime;
import com.sun.enterprise.security.BaseRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.internal.api.Globals;

public class CustomRealm extends BaseRealm {
    private static final Logger LOG = Logger.getLogger(CustomRealm.class.getName());
    
    private ActiveDescriptor<ConnectorRuntime> connectorRuntimeDescriptor;
    private String datasourceJNDI;
    
    @Override
    protected synchronized void init(Properties properties) throws BadRealmException, NoSuchRealmException {
        super.init(properties);
        
        //properties are not copied by default in the super-class context
        //especially needed for jaas-context and property retrieval in the SAINetLoginModule
        getProperties().putAll(properties);
        
        //copied from JDBCRealm
        connectorRuntimeDescriptor = (ActiveDescriptor<ConnectorRuntime>)Globals.getStaticHabitat().getBestDescriptor(BuilderHelper.createContractFilter(ConnectorRuntime.class.getName()));
        datasourceJNDI = properties.getProperty("datasource-jndi");
    }
    
    @Override
    public String getAuthType() {
        return "Custom Realm";
    }

    @Override
    public Enumeration<String> getGroupNames(String username) throws NoSuchUserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Authenticate the {@code username}, if possible.
     * If the {@code password} does not map to a JWT token, then this method will
     * return null.
     */
    public String[] authenticate(String username, char[] password) throws LoginException {
        if(username.equals("admin")) {
            try(Connection c = getConnection();
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery("SELECT * FROM sysuser_category WHERE userpk='admin'")) {
                List<String> roles = new ArrayList<>();
                while(r.next()) {
                    String role = r.getString("roleid");
                    roles.add(role);
                }
                return roles.toArray(new String[0]);
            } catch(SQLException sqe) {
                throw new LoginException("Unable to connect");
            }
        } else {
            return null;
        }
    }
    
    //copied from JDBCRealm
    private Connection getConnection() throws LoginException {
        try {
            ConnectorRuntime connectorRuntime = Globals.getStaticHabitat().getServiceHandle(connectorRuntimeDescriptor).getService();
            DataSource dataSource = (DataSource)connectorRuntime.lookupNonTxResource(datasourceJNDI, false);
            return dataSource.getConnection();
        } catch(SQLException | NamingException e) {
            LOG.log(Level.SEVERE, "Unable to access database", e);
            throw new LoginException("Database access failure");
        }
    }
}
