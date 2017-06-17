package techservetoday.fit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import techservetoday.pogo.FBUserFeed;
import techservetoday.pogo.FBUserFeedList;

class Pageradapter extends PagerAdapter {

	Activity mContext;
	LayoutInflater mLayoutInflater;
	private ListView l1;
	AdapterFacebookFeed mAdapter;
    String userFirstName;
    String userLastName;
    FBUserFeedList objFBUserFeedList;
    String picture;
    String[] values = new String[]{"Android List View",
            "Adapter implementation",
            "Simple List View In Android",
            "Create List View Android",
            "Android Example",
            "List View Source Code",
            "List View Array Adapter",
            "Android Example List View"
    };
    ArrayList<FBUserFeed> objList;



	public Pageradapter(Activity context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objFBUserFeedList= new FBUserFeedList();
         objList =new ArrayList<FBUserFeed>();




	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View itemView = mLayoutInflater.inflate(R.layout.pageritem, container,
				false);
        TextView t7 = (TextView) itemView.findViewById(R.id.textView7);
        l1 = (ListView) itemView.findViewById(R.id.listView);
        Log.d("page postition",""+position);
        if(position == 0) {

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/"+AccessToken.getCurrentAccessToken().getUserId()+"/feed",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            /* handle the result */

                            Log.d("UserFeed",response.toString());


                            try {
                                JSONArray  obj = (JSONArray) response.getJSONObject().get("data");


                                for(int i = 0 ;i<obj.length() ;i++) {
                                    JSONObject jObj = (JSONObject) obj.get(i);
                                    FBUserFeed objUserFeed = new FBUserFeed();
                                    objUserFeed.setId(jObj.get("id").toString());
                                    if(jObj.has("created_time"))
                                    objUserFeed.setCreated_time(jObj.get("created_time").toString());
                                    if(jObj.has("story"))
                                    objUserFeed.setStory(jObj.get("story").toString());
                                    if(jObj.has("message"))
                                    objUserFeed.setMessage(jObj.get("message").toString());

                                    objList.add(objUserFeed);
                                    Log.d("forloop",i+"");
                                    Log.d("forloopID",objUserFeed.getId());
                                }
                                Log.d("forloopI0D",objList.get(0).getId());
                              //  objFBUserFeedList.setFBUserFeedList(objList);

                              ///  Gson g = new Gson();
                               // objFBUserFeedList = g.fromJson(response.getJSONObject().toString(), FBUserFeedList.class);
                               // Log.e("UserFeedObj", response.getJSONObject().toString());
                                if(userFirstName!=null && userLastName!=null && objList!=null)  {
                                    Log.d("inside1","1");
                                    mAdapter = new AdapterFacebookFeed(objList, mContext, picture, userFirstName + " " + userLastName);

                                    l1.setAdapter(mAdapter);
                                }
                                //JSONObject jObj = (JSONObject) obj.get(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
            ).executeAsync();


            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.e("Pager",object.toString());
                    Log.e("Pager",response.toString());

                    String picture11 = "";
                    try {
                         userLastName = object.getString("last_name");
                        userFirstName = object.getString("first_name");
                         picture11 = object.getString("picture");
                        Log.e("Pager",picture11);
                        JSONObject profile1 = new JSONObject(picture11);
                        Log.e("Pager",profile1.toString());
                        String picture1 = profile1.getString("data");
                        JSONObject profile11 = new JSONObject(picture1);
                         picture = profile11.getString("url");
                        Log.d("picture",picture);
                        Log.d("userFirstName",userFirstName);
                        /* if(userFirstName!=null && userLastName!=null && objList!=null) {
                            Log.d("inside2","2");
                            mAdapter = new AdapterFacebookFeed(objList, mContext, picture, userFirstName + " " + userLastName);

                            l1.setAdapter(mAdapter);
                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
            //Here we put the requested fields to be returned from the JSONObject
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, birthday, gender,picture");
            request.setParameters(parameters);
            request.executeAsync();




            t7.setText("Twitter");

            if(userFirstName!=null && userLastName!=null && objList!=null) {
                Log.d("inside3","3");
                mAdapter = new AdapterFacebookFeed(objList, mContext, picture, userFirstName + " " + userLastName);

                l1.setAdapter(mAdapter);
            }
        }else if(position == 1){
            t7.setText("Facebook");
        }

		container.addView(itemView);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ActivityFBPost.class);
                intent.putExtra("ID", objList.get(position).getId());
                intent.putExtra("message", objList.get(position).getMessage());
                intent.putExtra("story", objList.get(position).getStory());
                intent.putExtra("name",  userFirstName + " " + userLastName);
                intent.putExtra("pic", picture);
               mContext.startActivity(intent);
            }
        });

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout) object);
	}

}