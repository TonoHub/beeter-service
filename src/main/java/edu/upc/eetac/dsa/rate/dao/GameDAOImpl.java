package edu.upc.eetac.dsa.rate.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.rate.entity.Game;
import edu.upc.eetac.dsa.rate.entity.GameCollection;

import java.sql.*;

/**
 * Created by tono on 28/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class GameDAOImpl implements GameDAO {

    @Override
    public Game createGame(String name, String genre, Integer score, Integer year) throws SQLException {
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

            stmt = connection.prepareStatement(GameDAOQuery.CREATE_GAME);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, genre);
            stmt.setInt(4, score);
            stmt.setInt(5, year);
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
        return getGameById(id);
    }

    @Override
    public Game getGameById(String id) throws SQLException {
        Game topic = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GameDAOQuery.GET_GAME_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                topic = new Game();
                topic.setId(rs.getString("id"));
                topic.setName(rs.getString("name"));
                topic.setGenre(rs.getString("genre"));
                topic.setScore(rs.getInt("score"));
                topic.setYear(rs.getInt("year"));
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
    public GameCollection getGame(long timestamp, boolean before) throws SQLException {
        GameCollection stingCollection = new GameCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(before)
                stmt = connection.prepareStatement(GameDAOQuery.GET_GAME);
            else
                stmt = connection.prepareStatement(GameDAOQuery.GET_GAME_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Game sting = new Game();
                sting.setId(rs.getString("id"));
                sting.setName(rs.getString("name"));
                sting.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                sting.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    stingCollection.setNewestTimestamp(sting.getLastModified());
                    first = false;
                }
                stingCollection.setOldestTimestamp(sting.getLastModified());
                stingCollection.getGame().add(sting);
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
    public Game updateGame(String id, String name, String genre, Integer score, Integer year) throws SQLException {
        Game topic = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GameDAOQuery.UPDATE_GAME);
            stmt.setString(1, name);
            stmt.setString(2, genre);
            stmt.setInt(3, score);
            stmt.setInt(4, year);
            stmt.setString(5, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                topic = getGameById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return topic;
    }

    @Override
    public boolean deleteGame(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GameDAOQuery.DELETE_GAME);
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

