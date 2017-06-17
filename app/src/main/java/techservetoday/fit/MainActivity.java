package techservetoday.fit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

import twitter4j.auth.RequestToken;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ImageView loginButton,btnLoginTwitter;
    private TextView getPosts;

    private String userId;
    private String TAG = "MainActivity";
    private URL profilePicture;

    // Internet Connection detector
    private ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        loginButton = (ImageView) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "user_birthday","user_posts"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Intent i = new Intent(getApplicationContext(),
                        ActivityDashBoard.class);
                startActivity(i);



             /*   GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,object.toString());
                        Log.e(TAG,response.toString());

                        try {
                            userId = object.getString("id");
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(ConstantValues.PREFERENCE_FACEBOOK_USESR_ID, userId);
                            editor.commit();
                           // profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");

                            Log.d(TAG, "onCompleted: "+userId );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                request.setParameters(parameters);
                request.executeAsync();*/

            }

            @Override
            public void onCancel() {
                Log.d("logcode", "inside cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("logcode", "inside on error");
            }
        });

        /*getPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserFeed();
            }
        });*/

        btnLoginTwitter = (ImageView) findViewById(R.id.btnLoginTwitter);

        /**
         * Twitter login button click event will call logIn() function
         * */
        btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Call login twitter function
                logIn();
            }
        });
    }

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            new loginToTwitter().execute();
            Log.d("logIn","1");
        }
        else
        {
            Log.d("logIn","2");
            Intent intent = new Intent(this, TwitterActivity.class);
            startActivity(intent);
        }
    }







    class loginToTwitter extends AsyncTask<String, String,RequestToken> {

        private Exception exception;

        //@Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {
                Log.d(TAG,requestToken.toString());

                {
                    Log.d("loginToTwitter","2");
                    Intent intent = new Intent(getApplicationContext(), OAuthActivity.class);
                    intent.putExtra(ConstantValues.URL_TWITTER_AUTH,requestToken.getAuthenticationURL());
                    Log.d(TAG,requestToken.getAuthenticationURL());
                    startActivity(intent);


               }
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }


    }




    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    private void getUserFeed() {

        new GraphRequest(
                com.facebook.AccessToken.getCurrentAccessToken(), "/" + userId + "/feed", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e(TAG, response.toString());

                        try {

                            JSONArray obj = (JSONArray) response.getJSONObject().get("data");
                            JSONObject jObj = (JSONObject) obj.get(0);
                            Log.e(TAG, jObj.get("id").toString());
                            getPost(jObj.get("id").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }

    private void getPost(String postId){

        new GraphRequest(
                com.facebook.AccessToken.getCurrentAccessToken(), "/"+postId+"/attachments", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e(TAG,response.toString());

                        try {

                            JSONArray obj = (JSONArray)response.getJSONObject().get("data");
                            JSONObject jObj =(JSONObject) obj.get(0);
                            Log.e(TAG,jObj.get("id").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();


    }

}
