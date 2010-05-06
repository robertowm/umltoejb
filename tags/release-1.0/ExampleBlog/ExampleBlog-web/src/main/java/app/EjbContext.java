package app;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 *
 * @author robertowm
 */
public class EjbContext {

    private static EjbContext instance;
    private Context context;

    private EjbContext() {
    }

    public static synchronized EjbContext instance() {
        if (instance == null) {
            instance = new EjbContext();
        }
        return instance;
    }

    public <T> T getHomeInterface(String name) throws NamingException {
        Object home = getContext().lookup(name);
        T type = null;
        return (T) PortableRemoteObject.narrow(home, type.getClass());
    }

    public Context getContext() {
        if (context == null) {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            props.put(Context.PROVIDER_URL, "localhost:1099");
            try {
                context = new InitialContext(props);
            } catch (NamingException ex) {
            }
        }
        return context;
    }

    public void invalidateContext() {
        context = null;
    }
}
