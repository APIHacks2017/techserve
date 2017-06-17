package techservetoday.fit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by Logesh Vijay on 6/17/2017.
 */
public class TwitterActivity extends Activity {

    Button buttonUpdateStatus, buttonLogout;
    EditText editTextStatus;
    TextView textViewStatus, textViewUserName;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);
        initializeComponent();
        initControl();
    }

    private void initControl() {
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(ConstantValues.TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(ConstantValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            new TwitterGetAccessTokenTask().execute(verifier);
        } else
            new TwitterGetAccessTokenTask().execute("");
    }

    private void initializeComponent() {
        buttonUpdateStatus = (Button) findViewById(R.id.buttonUpdateStatus);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        editTextStatus = (EditText) findViewById(R.id.editTextStatus);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewUserName = (TextView) findViewById(R.id.textViewUserName);
        buttonUpdateStatus.setOnClickListener(buttonUpdateStatusOnClickListener);
        buttonLogout.setOnClickListener(buttonLogoutOnClickListener);
    }

    private View.OnClickListener buttonLogoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
            editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
            editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false);
            editor.commit();
            TwitterUtil.getInstance().reset();
            Intent intent = new Intent(TwitterActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener buttonUpdateStatusOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String status = editTextStatus.getText().toString();

                new TwitterUpdateStatusTask().execute(status);

            Twitter twitter = new TwitterFactory().getInstance();
            try {
                List<Status> statuses;
                String user;

                user = twitter.verifyCredentials().getScreenName();
                statuses = twitter.getUserTimeline();

                System.out.println("Showing @" + user + "'s user timeline.");
                for (Status status1 : statuses) {
                    System.out.println("@" + status1.getUser().getScreenName() + " - " + status1.getText());
                }
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to get timeline: " + te.getMessage());
                System.exit(-1);
            }




        }
    };

    class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String userName) {
            textViewUserName.setText(Html.fromHtml("<b> Welcome " + userName + "</b>"));


        }

        @Override
        protected String doInBackground(String... params) {

            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
            if (params[0]!=null) {
                try {

                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                    editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
                    editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
                    editor.commit();
                    return twitter.showUser(accessToken.getUserId()).getName();
                } catch (TwitterException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                try {
                    TwitterUtil.getInstance().setTwitterFactory(accessToken);
                    return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
                } catch (TwitterException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), "Tweet successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Tweet failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");


                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                    twitter4j.Status status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
                    return true;


            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return false;  //To change body of implemented methods use File | Settings | File Templates.

        }
    }
}