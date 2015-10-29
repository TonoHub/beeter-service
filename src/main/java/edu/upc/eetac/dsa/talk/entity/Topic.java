package edu.upc.eetac.dsa.talk.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.LoginResource;
import edu.upc.eetac.dsa.talk.TopicResource;
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
@InjectLinks({
        @InjectLink(resource = TalkRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Talk Root API"),
        @InjectLink(resource = TopicResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-topic", title = "Current topic"),
        @InjectLink(resource = TopicResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-topic", title = "Create topic", type = MediaType.APPLICATION_FORM_URLENCODED),
        @InjectLink(resource = TopicResource.class, method = "getTopic", style = InjectLink.Style.ABSOLUTE, rel = "self topic", title = "Topic", bindings = @Binding(name = "id", value = "${instance.id}")),
        @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
        @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", bindings = @Binding(name = "id", value = "${instance.userid}")),
        @InjectLink(resource = TopicResource.class, method = "getTopic", style = InjectLink.Style.ABSOLUTE, rel = "next", title = "Newer topic", bindings = {@Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "false")}),
        @InjectLink(resource = TopicResource.class, method = "getTopic", style = InjectLink.Style.ABSOLUTE, rel = "previous", title = "Older topic", bindings = {@Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "true")}),
})
public class Topic {
    private List<Link> links;
    private String id;
    private String userid;
    private String groupid;
    private String creator;
    private String subject;
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

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupid() {return groupid;}

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

