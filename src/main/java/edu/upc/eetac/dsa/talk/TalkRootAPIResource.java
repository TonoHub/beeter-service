package edu.upc.eetac.dsa.talk;

import edu.upc.eetac.dsa.talk.entity.TalkRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by tono on 14/10/2015.
 */
@Path("/")
public class TalkRootAPIResource {
    @Context
    private SecurityContext securityContext;

    private String userid;

    @GET
    @Produces(TalkMediaType.TALK_ROOT)
    public TalkRootAPI getRootAPI() {
        if(securityContext.getUserPrincipal()!=null)
            userid = securityContext.getUserPrincipal().getName();
        TalkRootAPI talkRootAPI = new TalkRootAPI();

        return talkRootAPI;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
