package techservetoday.pogo;

/**
 * Created by Logesh Vijay on 6/17/2017.
 */
public class FBUserFeed {

    private String id;
    private String created_time;
    private String story;
    private String message;

    public FBUserFeed() {
    }

    public FBUserFeed(String id, String created_time, String story, String message) {
        this.id = id;
        this.created_time = created_time;
        this.story = story;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
