package techservetoday.fit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityFBPost extends AppCompatActivity {

    String postpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_fbpost);

        String id =  getIntent().getStringExtra("ID");
        String message =  getIntent().getStringExtra("message");
        String story =  getIntent().getStringExtra("story");
        String name =  getIntent().getStringExtra("name");
        String pic =  getIntent().getStringExtra("pic");


        ImageView tName = (ImageView) findViewById(R.id.ProfilePic);
        final ImageView tPostPic = (ImageView) findViewById(R.id.feedImage1);
        TextView tUserName = (TextView) findViewById(R.id.UserName);
        TextView tStory = (TextView) findViewById(R.id.story);
        TextView tMessage = (TextView) findViewById(R.id.message);

        Glide.with(ActivityFBPost.this)
                .load(pic)
                .placeholder(R.drawable.facebook_img)
                .error(R.drawable.facebook_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(tName);



        new GraphRequest(
                com.facebook.AccessToken.getCurrentAccessToken(), "/"+id+"/attachments", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e("last",response.toString());

                        try {

                            JSONArray obj = (JSONArray)response.getJSONObject().get("data");
                            JSONObject jObj =(JSONObject) obj.get(0);
                            JSONObject iobjj = jObj.getJSONObject("media");
                            JSONObject iobjjj = iobjj.getJSONObject("image");
                            Log.e("lastmedia",iobjjj.get("src").toString());
                            postpic = iobjjj.get("src").toString();
                            Glide.with(ActivityFBPost.this)
                                    .load(postpic)
                                    .placeholder(R.drawable.facebook_img)
                                    .error(R.drawable.facebook_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(tPostPic);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();




        tUserName.setText(name);
        tStory.setText(message);
        tMessage.setText(story);

    }
}
