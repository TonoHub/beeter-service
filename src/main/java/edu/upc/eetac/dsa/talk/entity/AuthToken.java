package edu.upc.eetac.dsa.talk.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by tono on 07/10/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public class AuthToken {

    @InjectLinks({
            @InjectLink(resource = TalkRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Beeter Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self login", title = "Login", type= TalkMediaType.TALK_AUTH_TOKEN),
            @InjectLink(resource = StingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-stings", title = "Current stings", type= TalkMediaType.TALK_STING_COLLECTION),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = StingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-sting", title = "Create sting", type= TalkMediaType.TALK_STING),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", type= TalkMediaType.TALK_USER, bindings = @Binding(name = "id", value = "${instance.userid}"))
    })
    private List<Link> links;

    private String userid;
    private String token;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
