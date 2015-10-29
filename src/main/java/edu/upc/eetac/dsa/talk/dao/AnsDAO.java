package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.talk.entity.Ans;
import edu.upc.eetac.dsa.talk.entity.AnsCollection;

import java.sql.SQLException;

/**
 * Created by tono on 27/10/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public interface AnsDAO {
    public Ans createAns(String userid, String topicid, String content) throws SQLException;
    public Ans getAnsById(String id) throws SQLException;
    public AnsCollection getAns (long timestamp, boolean before) throws SQLException;
    public Ans updateAns(String id, String content) throws SQLException;
    public boolean deleteAns(String id) throws SQLException;
}
