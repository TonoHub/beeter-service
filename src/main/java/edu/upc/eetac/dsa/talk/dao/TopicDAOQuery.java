package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by tono on 27/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public interface TopicDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_TOPIC = "insert into topic (id, userid, groupid, subject, content) values (UNHEX(?), unhex(?), unhex(?), ?, ?)";
    public final static String GET_TOPIC_BY_ID = "select hex(s.id) as id, hex(s.userid) as userid, hex(s.groupid) as groupid, s.content, s.subject, s.creation_timestamp, s.last_modified, u.fullname from topic s, users u where s.id=unhex(?) and u.id=s.userid";
    public final static String GET_TOPIC = "select hex(id) as id, hex(userid) as userid, hex(groupid) as groupid, subject, creation_timestamp, last_modified from topic where creation_timestamp < ? order by creation_timestamp desc limit 5";
    public final static String GET_TOPIC_AFTER = "select hex(id) as id, hex(userid) as userid, hex(groupid) as groupid, subject, creation_timestamp, last_modified from topic  where creation_timestamp > ? order by creation_timestamp desc limit 5";
    public final static String UPDATE_TOPIC = "update topic set subject=?, content=? where id=unhex(?) ";
    public final static String DELETE_TOPIC = "delete from topic where id=unhex(?)";
}
