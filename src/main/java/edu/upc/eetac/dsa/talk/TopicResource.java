package edu.upc.eetac.dsa.talk;

import edu.upc.eetac.dsa.talk.dao.TopicDAO;
import edu.upc.eetac.dsa.talk.dao.TopicDAOImpl;
import edu.upc.eetac.dsa.talk.entity.AuthToken;
import edu.upc.eetac.dsa.talk.entity.Topic;
import edu.upc.eetac.dsa.talk.entity.TopicCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by tono on 28/10/2015.
 */
@Path("topic")
public class TopicResource {
    @Context
    private SecurityContext securityContext;


    @POST
    public Response createTopic(@FormParam("groupid") String groupid, @FormParam("subject") String subject, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (subject == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        TopicDAO stingDAO = new TopicDAOImpl();
        Topic sting = null;
        AuthToken authenticationToken = null;
        try {
            sting = stingDAO.createTopic(securityContext.getUserPrincipal().getName(), groupid, subject, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + sting.getId());
        return Response.created(uri).type(TalkMediaType.TALK_TOPIC).entity(sting).build();
    }

    @GET
    @Produces(TalkMediaType.TALK_TOPIC_COLLECTION)
    public TopicCollection getTopic(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        TopicCollection stingCollection = null;
        TopicDAO stingDAO = new TopicDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            stingCollection = stingDAO.getTopic(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return stingCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(TalkMediaType.TALK_TOPIC)
    public Response getTopic(@PathParam("id") String id, @Context Request request) {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        Topic sting = null;
        TopicDAO stingDAO = new TopicDAOImpl();
        try {
            sting = stingDAO.getTopicById(id);
            if (sting == null)
                throw new NotFoundException("Topic with id = " + id + " doesn't exist");

            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(sting.getLastModified()));

            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

            // If ETag matches the rb will be non-null;
            // Use the rb to return the response without any further processing
            if (rb != null) {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }

            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(sting).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }


    @Path("/{id}")
    @PUT
    @Consumes(TalkMediaType.TALK_TOPIC)
    @Produces(TalkMediaType.TALK_TOPIC)
    public Topic updateUTopic(@PathParam("id") String id, Topic sting) {
        if(sting == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(sting.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(sting.getUserid()))
            throw new ForbiddenException("operation not allowed");

        TopicDAO stingDAO = new TopicDAOImpl();
        try {
            sting = stingDAO.updateTopic(id, sting.getSubject(), sting.getContent());
            if(sting == null)
                throw new NotFoundException("Topic with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }

    @Path("/{id}")
    @DELETE
    public void deleteTopic(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        TopicDAO stingDAO = new TopicDAOImpl();
        try {
            String ownerid = stingDAO.getTopicById(id).getUserid();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!stingDAO.deleteSting(id))
                throw new NotFoundException("Topic with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}

