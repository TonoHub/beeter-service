package edu.upc.eetac.dsa.talk;

import edu.upc.eetac.dsa.talk.dao.AnsDAO;
import edu.upc.eetac.dsa.talk.dao.AnsDAOImpl;
import edu.upc.eetac.dsa.talk.entity.AuthToken;
import edu.upc.eetac.dsa.talk.entity.Ans;
import edu.upc.eetac.dsa.talk.entity.AnsCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by tono on 28/10/2015.
 */
@Path("ans")
public class AnsResource {
    @Context
    private SecurityContext securityContext;


    @POST
    public Response createAns(@FormParam("topicid") String topicid, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (content == null)
            throw new BadRequestException("all parameters are mandatory");
        AnsDAO stingDAO = new AnsDAOImpl();
        Ans sting = null;
        AuthToken authenticationToken = null;
        try {
            sting = stingDAO.createAns(securityContext.getUserPrincipal().getName(),topicid,content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + sting.getId());
        return Response.created(uri).type(TalkMediaType.TALK_ANS).entity(sting).build();
    }

    @GET
    @Produces(TalkMediaType.TALK_ANS_COLLECTION)
    public AnsCollection getAns(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        AnsCollection stingCollection = null;
        AnsDAO stingDAO = new AnsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            stingCollection = stingDAO.getAns(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return stingCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(TalkMediaType.TALK_ANS)
    public Response getAns(@PathParam("id") String id, @Context Request request) {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        Ans sting = null;
        AnsDAO stingDAO = new AnsDAOImpl();
        try {
            sting = stingDAO.getAnsById(id);
            if (sting == null)
                throw new NotFoundException("Ans with id = " + id + " doesn't exist");

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
    @Consumes(TalkMediaType.TALK_ANS)
    @Produces(TalkMediaType.TALK_ANS)
    public Ans updateUAns(@PathParam("id") String id, Ans sting) {
        if(sting == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(sting.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(sting.getUserid()))
            throw new ForbiddenException("operation not allowed");

        AnsDAO stingDAO = new AnsDAOImpl();
        try {
            sting = stingDAO.updateAns(id, sting.getContent());
            if(sting == null)
                throw new NotFoundException("Ans with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }

    @Path("/{id}")
    @DELETE
    public void deleteAns(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        AnsDAO stingDAO = new AnsDAOImpl();
        try {
            String ownerid = stingDAO.getAnsById(id).getUserid();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!stingDAO.deleteAns(id))
                throw new NotFoundException("Ans with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}

