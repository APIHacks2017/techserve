package techservetoday.fit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Logesh Vijay on 6/17/2017.
 */
public class OAuthActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String authenticationUrl = getIntent().getStringExtra(ConstantValues.URL_TWITTER_AUTH);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        OAuthWebViewFragment oAuthWebViewFragment = new OAuthWebViewFragment(authenticationUrl);
        fragmentTransaction.add(android.R.id.content,oAuthWebViewFragment);

        fragmentTransaction.commit();
    }
}
