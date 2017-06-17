package techservetoday.fit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import techservetoday.pogo.FBUserFeed;
import techservetoday.pogo.FBUserFeedList;

public class AdapterFacebookFeed extends BaseAdapter {


	Activity c;
	String[] string;
    String userProfilePic;
    String userName;
    ArrayList<FBUserFeed> objFBUserFeed;

	public AdapterFacebookFeed(ArrayList<FBUserFeed> objFBUserFeed, Activity activity, String userProfilePic, String userName) {

		this.string =string;
		this.c = activity;
        this.userProfilePic=userProfilePic;
        this.userName=userName;
        this.objFBUserFeed=objFBUserFeed;



	}

	@Override
	public int getCount() {
		return objFBUserFeed.size();
	}

	@Override
	public Object getItem(int position) {


		return objFBUserFeed.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		LayoutInflater l = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = l.inflate(R.layout.facebookfeedlist, null);

        Log.d("name",userName);
        Log.d("userProfilePic",userProfilePic);
       // Log.d("getMessage",this.objFBUserFeed.get(position).getMessage());
        //Log.d("getStory",this.objFBUserFeed.get(position).getStory());
        ImageView tName = (ImageView) convertView.findViewById(R.id.ProfilePic);
        TextView tUserName = (TextView) convertView.findViewById(R.id.UserName);
		TextView tStory = (TextView) convertView.findViewById(R.id.story);
		TextView tMessage = (TextView) convertView.findViewById(R.id.message);
        Glide.with(c)
                .load(userProfilePic)
                .placeholder(R.drawable.facebook_img)
                .error(R.drawable.facebook_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(tName);

        tUserName.setText(userName);
        if(objFBUserFeed.get(position).getStory()!=null)
		tStory.setText(this.objFBUserFeed.get(position).getStory());
        if(objFBUserFeed.get(position).getMessage()!=null)
        tMessage.setText(this.objFBUserFeed.get(position).getMessage());


		return convertView;
	}


}
