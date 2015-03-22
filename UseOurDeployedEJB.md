# How to use our deployed EJBs? #

Simple! When you create the EJB context, use the following code:

```
...
    public Context getContext() {
        if (context == null) {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            props.put(Context.PROVIDER_URL, "200.20.1.103:1099");
            try {
                context = new InitialContext(props);
            } catch (NamingException ex) {
                // treat this exception...
            }
        }
        return context;
    }
...
```

For more details, you can see the example's code [here](http://code.google.com/p/umltoejb/wiki/ViewAnExample).
In the example, we use 'localhost' instead of '200.20.1.103' because the website and the ejbs are in the same application server.

`*``*``*`
_**We recommend to avoid using our EJBs. It is possible that this IP address change in the future.**_
`*``*``*`