package edu.upc.eetac.dsa.talk.entity;

import edu.upc.eetac.dsa.talk.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by tono on 14/10/2015.
 */



public class TalkRootAPI {
    @InjectLinks({
            @InjectLink(resource = TalkRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self bookmark home", title = "Talk Root API")
            ,@InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login",  type= TalkMediaType.TALK_AUTH_TOKEN)
            ,@InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-user", title = "Register", type= TalkMediaType.TALK_AUTH_TOKEN)
            ,@InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty resource.userid}")
            ,@InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", condition="${!empty resource.userid}", type= TalkMediaType.TALK_USER, bindings = @Binding(name = "id", value = "${resource.userid}"))
    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
