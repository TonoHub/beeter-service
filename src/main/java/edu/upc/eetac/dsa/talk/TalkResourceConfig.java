package edu.upc.eetac.dsa.talk;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Created by tono on 05/10/2015.
 */
public class TalkResourceConfig extends ResourceConfig {
    public TalkResourceConfig() {
        packages("edu.upc.eetac.dsa.talk");
        packages("edu.upc.eetac.dsa.talk.auth");
        packages("edu.upc.eetac.dsa.talk.cors");
        register(RolesAllowedDynamicFeature.class);
        register(DeclarativeLinkingFeature.class);
    }
}
