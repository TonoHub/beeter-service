package edu.upc.eetac.dsa.talk.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.LoginResource;
import edu.upc.eetac.dsa.talk.AnsResource;
import edu.upc.eetac.dsa.talk.TalkRootAPIResource;
import edu.upc.eetac.dsa.talk.UserResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by tono on 27/10/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Ans {
    @InjectLinks({
            @InjectLink(resource = TalkRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Talk Root API"),
            @InjectLink(resource = AnsResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-ans", title = "Current ans"),
            @InjectLink(resource = AnsResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-ans", title = "Create ans", type = MediaType.APPLICATION_FORM_URLENCODED),
            @InjectLink(resource = AnsResource.class, method = "getAns", style = InjectLink.Style.ABSOLUTE, rel = "self ans", title = "ans", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", bindings = @Binding(name = "id", value = "${instance.userid}")),
            @InjectLink(resource = AnsResource.class, method = "getAns", style = InjectLink.Style.ABSOLUTE, rel = "next", title = "Newer ans", bindings = {@Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "false")}),
            @InjectLink(resource = AnsResource.class, method = "getAns", style = InjectLink.Style.ABSOLUTE, rel = "previous", title = "Older ans", bindings = {@Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "true")}),
    })
    private List<Link> links;
    private String id;
    private String userid;
    private String topicid;
    private String creator;
    private String content;
    private long creationTimestamp;
    private long lastModified;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}

