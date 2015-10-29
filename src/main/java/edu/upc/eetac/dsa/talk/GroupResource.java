package edu.upc.eetac.dsa.talk;

import edu.upc.eetac.dsa.talk.dao.GroupDAO;
import edu.upc.eetac.dsa.talk.dao.GroupDAOImpl;
import edu.upc.eetac.dsa.talk.entity.AuthToken;
import edu.upc.eetac.dsa.talk.entity.Group;
import edu.upc.eetac.dsa.talk.entity.GroupCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by tono on 28/10/2015.
 */
@Path("groups")
public class GroupResource {

    @Context
    private SecurityContext securityContext;


    @POST
    public Response createGroup(@FormParam("subject") String subject, @Context UriInfo uriInfo) throws URISyntaxException {
        if (subject == null)
            throw new BadRequestException("all parameters are mandatory");
        GroupDAO stingDAO = new GroupDAOImpl();
        Group sting = null;
        AuthToken authenticationToken = null;
        try {
            sting = stingDAO.createGroup(securityContext.getUserPrincipal().getName(), subject);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + sting.getId());
        return Response.created(uri).type(TalkMediaType.TALK_GROUP).entity(sting).build();
    }

    @GET
    @Produces(TalkMediaType.TALK_GROUP_COLLECTION)
    public GroupCollection getGroup(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        GroupCollection stingCollection = null;
        GroupDAO stingDAO = new GroupDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            stingCollection = stingDAO.getGroup(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return stingCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(TalkMediaType.TALK_GROUP)
    public Response getGroup(@PathParam("id") String id, @Context Request request) {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        Group sting = null;
        GroupDAO stingDAO = new GroupDAOImpl();
        try {
            sting = stingDAO.getGroupById(id);
            if (sting == null)
                throw new NotFoundException("Group with id = " + id + " doesn't exist");

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
    @Consumes(TalkMediaType.TALK_GROUP)
    @Produces(TalkMediaType.TALK_GROUP)
    public Group updateUGroup(@PathParam("id") String id, Group sting) {
        if(sting == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(sting.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(sting.getUserid()))
            throw new ForbiddenException("operation not allowed");

        GroupDAO stingDAO = new GroupDAOImpl();
        try {
            sting = stingDAO.updateGroup(id, sting.getSubject());
            if(sting == null)
                throw new NotFoundException("Group with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }

    @Path("/{id}")
    @DELETE
    public void deleteGroup(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        GroupDAO stingDAO = new GroupDAOImpl();
        try {
            String ownerid = stingDAO.getGroupById(id).getUserid();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!stingDAO.deleteGroup(id))
                throw new NotFoundException("Group with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}

