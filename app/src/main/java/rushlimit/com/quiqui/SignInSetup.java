package rushlimit.com.quiqui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Arman on 3/15/15.
 */
public class SignInSetup extends ActionBarActivity {

    private Spinner major, minor;
    private TextView name;
    private String chosenMajor;
    private String chosenMinor;
    private Button done;
    SharedPreferences sP;
    SharedPreferences.Editor editor;
    private Toolbar mToolbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_setup);

//        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
//        setSupportActionBar(mToolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
//        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.signin_up);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sP = getSharedPreferences("rushlimit.com.quiqui", MODE_PRIVATE);
        editor = sP.edit();

        major = (Spinner) findViewById(R.id.majors);
        minor = (Spinner) findViewById(R.id.minors);
        done = (Button) findViewById(R.id.done);
        name = (TextView) findViewById(R.id.name);

        if(sP.getBoolean("gmail", false)) {
            name.setText(sP.getString("gmailFName", "") + " " + sP.getString("gmailLName", ""));
        } else if(sP.getBoolean("facebook", false)) {
            name.setText(sP.getString("facebookFName", "") + " " + sP.getString("facebookLName", ""));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Majors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        major.setAdapter(adapter);

        major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenMajor = parent.getItemAtPosition(position).toString();
                if(!chosenMajor.equals("<Choose a Major>"))
                    Toast.makeText(getApplicationContext(), chosenMajor, Toast.LENGTH_SHORT).show();
                editor.putString("chosenMajor", chosenMajor);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = null;
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.Minors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        minor.setAdapter(adapter);

        minor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenMinor = parent.getItemAtPosition(position).toString();
                if(!chosenMinor.equals("<Choose a Minor>"))
                    Toast.makeText(getApplicationContext(), chosenMinor, Toast.LENGTH_SHORT).show();
                editor.putString("chosenMinor", chosenMinor);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenMajor.equals("<Choose a Major>")) {
                    Toast.makeText(getApplicationContext(),
                            "You must choose a Major", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("Major", chosenMajor);
                    if(!chosenMinor.equals("<Choose a Minor>"))
                        editor.putString("Minor", chosenMinor);
                    editor.apply();

                    ////////////////////////////
                    // finish() to return to slideoutmenu
                    ////////////////////////////

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }



}
