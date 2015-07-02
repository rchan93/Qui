package rushlimit.com.quiqui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    SharedPreferences sP;
    SharedPreferences.Editor editor;

    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private FragmentActivity myContext;
    private RecyclerView recyclerView;
    private DrawerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sP = getActivity().getSharedPreferences(
                "rushlimit.com.quiqui", Activity.MODE_PRIVATE);

        MainActivity.userSignedIn = sP.getBoolean("signedIn", false);
        Log.d("QUI", MainActivity.userSignedIn + " onCreate Main");

        if (!sP.getBoolean("signedIn", false)) {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FragmentManager fm = myContext.getSupportFragmentManager();

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(fm));
        mTabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);

        Bundle bundle = getArguments();

//        Log.d("QUI", "Creating Fragment");
//        Button signout = (Button) rootView.findViewById(R.id.sign_out);
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SignUpActivity.class);
//                intent.putExtra("signedOut", true);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.userSignedIn = sP.getBoolean("signedIn", false);
        MainActivity.firstTime = sP.getBoolean("firstTime", true);

        ////////////////////////////
        // First time app is closed ; get checks for
        // presses back after sign in and no setup
        // presses back during setup
        ////////////////////////////

        Log.d("QUI", sP.getBoolean("signedIn", false) + " inOnResume what is?");
        if(!sP.getBoolean("signedIn", false))
            getActivity().finish();

    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.userSignedIn = sP.getBoolean("signedIn", false);
        MainActivity.firstTime = sP.getBoolean("firstTime", true);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabs[position];
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class MyFragment extends android.support.v4.app.Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstance){
            Bundle bundle = getArguments();
            int page = bundle.getInt("position");
            View layout = null;
            RecyclerView recyclerView;
            BookAdapter bookAdapter;

//            layout = inflater.inflate(R.layout.fragment_my, container, false);
//            textView = (TextView) layout.findViewById(R.id.position);

            if(bundle != null) {
//                textView.setText("This page: " + bundle.getInt("position"));
                switch(bundle.getInt("position")) {
                    case 0:
                        layout = inflater.inflate(R.layout.majorbooklist, container, false);
                        recyclerView = (RecyclerView) layout.findViewById(R.id.home_major_List);
                        bookAdapter = new BookAdapter(getActivity(), getBookData());
                        recyclerView.setAdapter(bookAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        break;
                    case 1:
                        layout = inflater.inflate(R.layout.majorbooklist, container, false);
                        recyclerView = (RecyclerView) layout.findViewById(R.id.home_major_List);
                        bookAdapter = new BookAdapter(getActivity(), getBookData2());
                        recyclerView.setAdapter(bookAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        break;
                    case 2:
                        layout = inflater.inflate(R.layout.majorbooklist, container, false);
                        recyclerView = (RecyclerView) layout.findViewById(R.id.home_major_List);
                        bookAdapter = new BookAdapter(getActivity(), getBookData3());
                        recyclerView.setAdapter(bookAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        break;
                }
            }

            return layout;
        }
    }

    public static List<BookInformation> getBookData() {
        List<BookInformation> data = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            BookInformation bi = new BookInformation("Intro to C++", "John Doe", "123-4-56-789012-3", "Moe Schmoe", "Good",
                                                            "Computer Science", "$50.00", R.drawable.ic_book);
            data.add(bi);
        }
        return data;
    }
    public static List<BookInformation> getBookData2() {
        List<BookInformation> data = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            BookInformation bi = new BookInformation("Data Structures", "John Doe", "123-4-56-789012-3", "Moe Schmoe", "Good",
                    "Computer Science", "$50.00", R.drawable.ic_book);
            data.add(bi);
        }
        return data;
    }
    public static List<BookInformation> getBookData3() {
        List<BookInformation> data = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            BookInformation bi = new BookInformation("Introduction to Psychology", "Bob Po", "943-4-83-789012-3", "Richurd Chu", "New",
                    "Psychology", "$25.00", R.drawable.ic_book);
            data.add(bi);
        }
        return data;
    }



//    public static List<Information> getData(){
//        List<Information> data = new ArrayList<>();
//        int []icons = {R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person,R.drawable.ic_action_person};
//        String[] titles = {"Home","Discover","Categories","Profile","Support","Setting","About Us","Logout"};
//
//        for(int i=0; i<titles.length && i<icons.length; i++){
//            Information current = new Information();
//            current.iconId = icons[i];
//            current.title = titles[i];
//            data.add(current);
//        }
//        return data;
//    }


}
