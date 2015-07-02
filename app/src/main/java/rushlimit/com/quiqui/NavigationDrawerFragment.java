package rushlimit.com.quiqui;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "testpref";

    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;

    //For rotation purposes
    private boolean mFromSavedInstanceState;

    private View containerView;

    private static ListView listView;
    private RecyclerView recyclerView;
    private DrawerAdapter adapter;

    private TextView name;
    private TextView email;
    private TextView major;
    static SharedPreferences sp;

    public NavigationDrawerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if(savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new DrawerAdapter(getActivity(), getData());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ImageView mChart = (ImageView) layout.findViewById(R.id.dProfilepic);

        name = (TextView)layout.findViewById(R.id.dName);
        email = (TextView)layout.findViewById(R.id.dEmail);
        major = (TextView)layout.findViewById(R.id.dMajor);

        sp = getActivity().getSharedPreferences("rushlimit.com.quiqui", Activity.MODE_PRIVATE);
        if(sp.getBoolean("gmail", false)) {
            name.setText("Name: " + sp.getString("gmailFName", "") + " " + sp.getString("gmailLName", ""));
            email.setText("Email: " + sp.getString("gmailEmail", "None"));
            major.setText("Major: " + sp.getString("chosenMajor", "None"));
            mChart.setTag(sp.getString("gmailPhoto", "http://www.discoverychurch.org/Portals/0/images/profiles/profile_default(800x600).jpg"));
        } else if(sp.getBoolean("facebook", false)) {
            name.setText("Name: " + sp.getString("facebookFName", "") + " " + sp.getString("facebookLName", ""));
            email.setText("Email: " + sp.getString("facebookEmail", "None"));
            major.setText("Major: " + sp.getString("chosenMajor", "None"));
            mChart.setTag(sp.getString("facebookPicture", "http://www.discoverychurch.org/Portals/0/images/profiles/profile_default(800x600).jpg"));
        }

        new DownloadImagesTask().execute(mChart);

        return layout;
    }

    public static List<Information> getData(){
        List<Information> data = new ArrayList<>();
        int []icons = {R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person};
        String[] titles = {"Home","Categories","My Listing","Profile","Support","Setting","About Us","Logout"};

        for(int i=0; i<titles.length && i<icons.length; i++){
            Information current = new Information();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentId, final DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;

                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.closeDrawer(Gravity.START);
                getActivity().supportInvalidateOptionsMenu();
            }
        };

//        if(!mUserLearnedDrawer && !mFromSavedInstanceState) {
//            mDrawerLayout.openDrawer(containerView);
//        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run(){
                mDrawerToggle.syncState();
                if(!mUserLearnedDrawer && !mFromSavedInstanceState) {
                    mDrawerLayout.openDrawer(containerView);
                }
            }
        });

    }

    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image((String) imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {
            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }
    }

    public void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceValue);

        //can use .commit but slower, apply is async. and won't tell you any errors
        editor.apply();

    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferenceName, defaultValue);

    }

}
