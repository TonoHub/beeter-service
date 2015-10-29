package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.entity.Topic;
import edu.upc.eetac.dsa.talk.entity.TopicCollection;

import java.sql.SQLException;

/**
 * Created by tono on 27/10/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public interface TopicDAO {
    public Topic createTopic(String userid, String groupid, String subject, String content) throws SQLException;
    public Topic getTopicById(String id) throws SQLException;
    public TopicCollection getTopic(long timestamp, boolean before) throws SQLException;
    public Topic updateTopic(String id, String subject, String content) throws SQLException;
    public boolean deleteSting(String id) throws SQLException;
}
