package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by tono on 27/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public interface GroupDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_GROUP = "insert into groups (id, userid, subject) values (UNHEX(?), unhex(?), ?)";
    public final static String GET_GROUP_BY_ID = "select hex(s.id) as id, hex(s.userid) as userid, s.subject, s.creation_timestamp, s.last_modified, u.fullname from groups s, users u where s.id=unhex(?) and u.id=s.userid";
    public final static String GET_GROUP = "select hex(id) as id, hex(userid) as userid, subject, creation_timestamp, last_modified from groups where creation_timestamp < ? order by creation_timestamp desc limit 5";
    public final static String GET_GROUP_AFTER = "select hex(id) as id, hex(userid) as userid, subject, creation_timestamp, last_modified from groups  where creation_timestamp > ? order by creation_timestamp desc limit 5";
    public final static String UPDATE_GROUP = "update groups set subject=? where id=unhex(?) ";
    public final static String DELETE_GROUP = "delete from groups where id=unhex(?)";
}
