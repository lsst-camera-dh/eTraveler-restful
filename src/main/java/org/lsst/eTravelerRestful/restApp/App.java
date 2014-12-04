
package org.lsst.eTravelerRestful.restApp;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * The basic entry point for a Jersey-based RESTful Application
 * @author bvan
 */
public class App extends ResourceConfig {
    
    /**
     * We want to configure Jersey to use Jackson by Default. 
     * In addition to this, we want to disable "moxy" JSON, 
     * which Jersey is configured to use by default, so there is no conflict.
     */
    public static class JacksonFeature implements Feature {

        @Override
        public boolean configure(final FeatureContext context){
            final String disableMoxy = CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.'
                    + context.getConfiguration().getRuntimeType().name().toLowerCase();
            context.property( disableMoxy, true );
            context.register( JacksonJaxbJsonProvider.class, MessageBodyReader.class, 
                    MessageBodyWriter.class );
            return true;
        }
    }
    
    public App(){
        super();
        register(JacksonFeature.class);
    }

}
