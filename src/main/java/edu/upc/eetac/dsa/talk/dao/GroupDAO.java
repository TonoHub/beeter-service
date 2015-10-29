package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.entity.Group;
import edu.upc.eetac.dsa.talk.entity.GroupCollection;

import java.sql.SQLException;

/**
 * Created by tono on 27/10/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public interface GroupDAO {
    public Group createGroup(String userid, String subject) throws SQLException;
    public Group getGroupById(String id) throws SQLException;
    public GroupCollection getGroup(long timestamp, boolean before) throws SQLException;
    public Group updateGroup(String id, String subject) throws SQLException;
    public boolean deleteGroup(String id) throws SQLException;
}
