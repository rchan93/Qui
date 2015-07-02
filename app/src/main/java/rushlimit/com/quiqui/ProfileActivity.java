package rushlimit.com.quiqui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileActivity extends ActionBarActivity {

    private Toolbar toolbar;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    TextView textViewName, textViewEmail;
    EditText editTextPhone;
    //EditText editViewMajor, editViewMinor;
    String name, major, minor, pNumber, email;

    Spinner spinnerMajor, spinnerMinor;

    Button edit;
    Boolean editClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sp = getSharedPreferences("rushlimit.com.quiqui", MODE_PRIVATE);
        editor = sp.edit();
        textViewName = (TextView) findViewById(R.id.name);
        spinnerMajor = (Spinner) findViewById(R.id.majors);
        spinnerMajor.setEnabled(false);
        spinnerMinor = (Spinner) findViewById(R.id.minors);
        spinnerMinor.setEnabled(false);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextPhone.setEnabled(false);
        textViewEmail = (TextView) findViewById(R.id.email);
        edit = (Button) findViewById(R.id.editButton);
        edit.setText("Edit");


        Log.d("Qui", "Gmail: " + sp.getBoolean("gmail", false));
        Log.d("Qui", "Facebook: " + sp.getBoolean("facebook", false));
        if(sp.getBoolean("gmail", false)) {
            textViewName.setText("Name: " + sp.getString("gmailFName", "") + " " + sp.getString("gmailLName", ""));
            editTextPhone.setText(sp.getString("phone", "N/A"));
            textViewEmail.setText("Email: " + sp.getString("gmailEmail", "None"));
        } else if(sp.getBoolean("facebook", false)) {
            textViewName.setText("Name: " + sp.getString("facebookFName", "") + " " + sp.getString("facebookLName", ""));
            textViewEmail.setText("Email: " + sp.getString("facebookEmail", "None"));
            editTextPhone.setText(sp.getString("phone", "N/A"));
        }

        //Set up navigation drawer
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), toolbar);

        // Create an ArrayAdapter using the string array and a default Spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Majors, android.R.layout.simple_spinner_item);
        // specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        int spinnerPosition = adapter.getPosition(sp.getString("chosenMajor", "N/A"));

        // Apply the adapter to the Spinner
        spinnerMajor.setAdapter(adapter);
        spinnerMajor.setSelection(spinnerPosition);

        spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                major = parent.getItemAtPosition(position).toString();
                editor.putString("chosenMajor", major);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = null;
        // Create an ArrayAdapter using the string array and a default Spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.Minors, android.R.layout.simple_spinner_item);
        // specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the Spinner
        spinnerPosition = adapter.getPosition(sp.getString("chosenMinor", "N/A"));
        spinnerMinor.setAdapter(adapter);
        spinnerMinor.setSelection(spinnerPosition);
        spinnerMinor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minor = parent.getItemAtPosition(position).toString();
                editor.putString("chosenMinor", minor);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClicked = !editClicked;

                if(editClicked) {
                    edit.setText("Save");
                    spinnerMajor.setEnabled(true);
                    spinnerMinor.setEnabled(true);
                    editTextPhone.setEnabled(true);
                }
                else {
                    edit.setText("Edit");
                    editor.putString("phone", editTextPhone.getText().toString());
                    spinnerMajor.setEnabled(false);
                    spinnerMinor.setEnabled(false);
                    editTextPhone.setEnabled(false);
                    editor.apply();
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                }
            }
        });


    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
