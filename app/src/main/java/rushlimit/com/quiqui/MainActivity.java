package rushlimit.com.quiqui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    /*
    BUGS:

    sign in with G+, press back button
    goes to mainfragment/activity with slide out menu open
    same with fb
b
    signing in with google sometimes farts (doesnt sign in and starts itself up again)
     */
    protected static boolean userSignedIn;
    protected static boolean firstTime;
    SharedPreferences sP;
    SharedPreferences.Editor editor;

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    NavigationDrawerFragment drawerFragment;
//    private NavigationDrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("QUI", "in onCreate");
        sP = getSharedPreferences("rushlimit.com.quiqui", MODE_PRIVATE);
        editor = sP.edit();

//        firstTime=true; //RESET VALUE FOR TESTING
//        editor.clear(); // RESET SHARED PREFERENCES FOR TESTING
        editor.putBoolean("firstTime", firstTime);
        userSignedIn = sP.getBoolean("signedIn", false);
        editor.apply();

        //Set up action bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Set up navigation drawer
        drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), toolbar);

        FragmentManager fM = getFragmentManager();
        FragmentTransaction fragment = fM.beginTransaction();
        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            fragment.add(R.id.container, mainFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        userSignedIn = sP.getBoolean("signedIn", false);
        firstTime = sP.getBoolean("firstTime", true);


    }

    @Override
    public void onStart() {
        super.onStart();
        userSignedIn = sP.getBoolean("signedIn", false);
        firstTime = sP.getBoolean("firstTime", true);
    }


    @Override
    public void onBackPressed() {
        /*
        if on home page close app (clear stack)
         */
        this.finish();
        //Intent int1= new Intent(this, MainActivity.class);
//        int1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(int1);
        super.onBackPressed();
    }
}
