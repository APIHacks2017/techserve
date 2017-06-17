package techservetoday.fit;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.net.URL;

public class ActivityDashBoard extends AppCompatActivity {

    private String TAG = "ActivityDashBoard";
    private URL profilePicture;
    static Pageradapter mCustomPagerAdapter;
    static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mCustomPagerAdapter = new Pageradapter(this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

    }
}
