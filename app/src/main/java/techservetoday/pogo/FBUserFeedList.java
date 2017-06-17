package techservetoday.pogo;

import java.util.List;

/**
 * Created by Logesh Vijay on 6/17/2017.
 */
public class FBUserFeedList {

    public List<FBUserFeed> FBUserFeedList;

    public FBUserFeedList() {

    }

    public FBUserFeedList(List<FBUserFeed> FBUserFeedList) {
        this.FBUserFeedList = FBUserFeedList;
    }

    public List<FBUserFeed> getFBUserFeedList() {
        return FBUserFeedList;
    }

    public void setFBUserFeedList(List<FBUserFeed> FBUserFeedList) {
        this.FBUserFeedList = FBUserFeedList;
    }


}