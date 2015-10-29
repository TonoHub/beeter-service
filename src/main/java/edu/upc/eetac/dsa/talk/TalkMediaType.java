package edu.upc.eetac.dsa.talk;

/**
 * Created by tono on 10/10/2015.
 */
public interface TalkMediaType {
    public final static String TALK_AUTH_TOKEN = "application/vnd.dsa.talk.auth-token+json";
    public final static String TALK_USER = "application/vnd.dsa.talk.user+json";
    public final static String TALK_ANS = "application/vnd.dsa.talk.ans+json";
    public final static String TALK_ANS_COLLECTION = "application/vnd.dsa.talk.ans.collection+json";
    public final static String TALK_TOPIC = "application/vnd.dsa.talk.topic+json";
    public final static String TALK_TOPIC_COLLECTION = "application/vnd.dsa.talk.topic.collection+json";
    public final static String TALK_GROUP = "application/vnd.dsa.talk.group+json";
    public final static String TALK_GROUP_COLLECTION = "application/vnd.dsa.talk.group.collection+json";
    public final static String TALK_STING = "application/vnd.dsa.talk.sting+json";
    public final static String TALK_STING_COLLECTION = "application/vnd.dsa.talk.sting.collection+json";
    public final static String TALK_ROOT = "application/vnd.dsa.talk.root+json";
}
