package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.entity.Topic;
import edu.upc.eetac.dsa.talk.entity.TopicCollection;

import java.sql.*;

/**
 * Created by tono on 28/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class TopicDAOImpl implements TopicDAO {

    @Override
    public Topic createTopic(String userid, String groupid, String subject, String content) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(TopicDAOQuery.CREATE_TOPIC);
            stmt.setString(1, id);
            stmt.setString(2, userid);
            stmt.setString(3,groupid);
            stmt.setString(4, subject);
            stmt.setString(5, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getTopicById(id);
    }

    @Override
    public Topic getTopicById(String id) throws SQLException {
        Topic topic = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(TopicDAOQuery.GET_TOPIC_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                topic = new Topic();
                topic.setId(rs.getString("id"));
                topic.setUserid(rs.getString("userid"));
                topic.setGroupid(rs.getString("groupid"));
                topic.setCreator(rs.getString("fullname"));
                topic.setSubject(rs.getString("subject"));
                topic.setContent(rs.getString("content"));
                topic.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                topic.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return topic;
    }

    @Override
    public TopicCollection getTopic(long timestamp, boolean before) throws SQLException {
        TopicCollection stingCollection = new TopicCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(before)
                stmt = connection.prepareStatement(TopicDAOQuery.GET_TOPIC);
            else
                stmt = connection.prepareStatement(TopicDAOQuery.GET_TOPIC_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Topic sting = new Topic();
                sting.setId(rs.getString("id"));
                sting.setUserid(rs.getString("userid"));
                sting.setGroupid(rs.getString("groupid"));
                sting.setSubject(rs.getString("subject"));
                sting.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                sting.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    stingCollection.setNewestTimestamp(sting.getLastModified());
                    first = false;
                }
                stingCollection.setOldestTimestamp(sting.getLastModified());
                stingCollection.getTopic().add(sting);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return stingCollection;
    }


    @Override
    public Topic updateTopic(String id, String subject, String content) throws SQLException {
        Topic topic = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(TopicDAOQuery.UPDATE_TOPIC);
            stmt.setString(1, subject);
            stmt.setString(2, content);
            stmt.setString(3, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                topic = getTopicById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return topic;
    }

    @Override
    public boolean deleteSting(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(TopicDAOQuery.DELETE_TOPIC);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}

