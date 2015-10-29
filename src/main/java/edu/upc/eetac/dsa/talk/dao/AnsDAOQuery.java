package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by tono on 27/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public interface AnsDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_ANS = "insert into ans (id, userid, topicid, content) values (UNHEX(?), unhex(?), unhex(?), ?)";
    public final static String GET_ANS_BY_ID = "select hex(s.id) as id, hex(s.userid) as userid, hex(s.topicid) as topicid, s.content, s.creation_timestamp, s.last_modified, u.fullname from ans s, users u where s.id=unhex(?) and u.id=s.userid";
    public final static String GET_ANS = "select hex(id) as id, hex(userid) as userid, hex(topicid) as topicid, creation_timestamp, last_modified from ans where creation_timestamp < ? order by creation_timestamp desc limit 5";
    public final static String GET_ANS_AFTER = "select hex(id) as id, hex(userid) as userid, hex(topicid) as topicid, creation_timestamp, last_modified from ans  where creation_timestamp > ? order by creation_timestamp desc limit 5";
    public final static String UPDATE_ANS = "update ans set content=? where id=unhex(?) ";
    public final static String DELETE_ANS = "delete from ans where id=unhex(?)";
}
