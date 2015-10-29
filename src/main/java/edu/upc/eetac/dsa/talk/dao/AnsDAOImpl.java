package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.entity.Ans;
import edu.upc.eetac.dsa.talk.entity.AnsCollection;

import java.sql.*;

/**
 * Created by tono on 28/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AnsDAOImpl implements AnsDAO {

    @Override
    public Ans createAns(String userid, String topicid, String content) throws SQLException {
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

            stmt = connection.prepareStatement(AnsDAOQuery.CREATE_ANS);
            stmt.setString(1, id);
            stmt.setString(2, userid);
            stmt.setString(3, topicid);
            stmt.setString(4, content);
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
        return getAnsById(id);
    }

    @Override
    public Ans getAnsById(String id) throws SQLException {
        Ans ans = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(AnsDAOQuery.GET_ANS_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ans = new Ans();
                ans.setId(rs.getString("id"));
                ans.setUserid(rs.getString("userid"));
                ans.setTopicid(rs.getString("topicid"));
                ans.setCreator(rs.getString("fullname"));
                ans.setContent(rs.getString("content"));
                ans.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                ans.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return ans;
    }

    @Override
    public AnsCollection getAns(long timestamp, boolean before) throws SQLException {
        AnsCollection stingCollection = new AnsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(before)
                stmt = connection.prepareStatement(AnsDAOQuery.GET_ANS);
            else
                stmt = connection.prepareStatement(AnsDAOQuery.GET_ANS_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Ans sting = new Ans();
                sting.setId(rs.getString("id"));
                sting.setUserid(rs.getString("userid"));
                sting.setTopicid(rs.getString("topicid"));
                sting.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                sting.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    stingCollection.setNewestTimestamp(sting.getLastModified());
                    first = false;
                }
                stingCollection.setOldestTimestamp(sting.getLastModified());
                stingCollection.getAns().add(sting);
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
    public Ans updateAns(String id, String content) throws SQLException {
        Ans ans = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(AnsDAOQuery.UPDATE_ANS);
            stmt.setString(1, content);
            stmt.setString(2, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                ans = getAnsById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return ans;
    }

    @Override
    public boolean deleteAns(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(AnsDAOQuery.DELETE_ANS);
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
