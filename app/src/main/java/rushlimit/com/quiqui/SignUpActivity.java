package rushlimit.com.quiqui;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class SignUpActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    //TODO: finish() signupactivity so that there are not multiple ones..

    // ----- Start Intitialization Process Google+

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    // ----- End Intitialization Process Google+


    // Start Google+ signing in

    static SignInButton mSignInButton;
    private Button signout;

    /* Track whether the sign-in button has been clicked so that we know to resolve
     * all issues preventing sign-in without waiting.
     */
    protected static boolean mSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
    * resolve them when the user clicks sign-in.
    */
    private ConnectionResult mConnectionResult;

    // End Google+ signing in

    // Start FB signing in
    //>
    private Session.StatusCallback statusCallback =
            new SessionStatusCallback();

    private UiLifecycleHelper uiHelper;

    // End FB signing in

    SharedPreferences sP;
    SharedPreferences.Editor editor;

    private Toolbar mToolbar;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
//        setSupportActionBar(mToolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

//        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.signin_up);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        sP = getSharedPreferences("rushlimit.com.quiqui",
                MODE_PRIVATE);
        editor = sP.edit();

        //>
        LoginButton authButton = (LoginButton) findViewById(R.id.fb_login);
        authButton.setReadPermissions(Arrays.asList("email"));

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

        //<

        Log.d("QUI", "Creating SignUp");

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.connect();
                mSignInClicked = true;
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    if(mGoogleApiClient.isConnected())
                        Toast.makeText(SignUpActivity.this, "You are connected", Toast.LENGTH_SHORT).show();
                    else
                        resolveSignInError();
                }
            }
        });

        signout = (Button) findViewById(R.id.sign_out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QUI", "Registers");
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }

                editor.putBoolean("signedIn", false).apply();

                MainActivity.userSignedIn = false;
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    // --- Google+

    @Override
    public void onConnected(Bundle bundle) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.

        mSignInClicked = false;
        //Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
//        finish();

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String fName = currentPerson.getName().getGivenName();
            String lName = currentPerson.getName().getFamilyName();
            String personPhoto = "";
            String coverURL = "";
            String personId = currentPerson.getId();
            if (currentPerson.hasImage()
                    && currentPerson.getImage().hasUrl()) {
                personPhoto = currentPerson.getImage().getUrl();
            }
            if (currentPerson.hasCover()
                    && currentPerson.getCover().hasCoverPhoto()
                    && currentPerson.getCover().getCoverPhoto().hasUrl()) {
                coverURL = currentPerson.getCover().getCoverPhoto().getUrl();
            }

            editor.putBoolean("gmail", true);
            editor.putString("gmailId", personId);
            editor.putString("gmailFName", fName);
            editor.putString("gmailLName", lName);
            editor.putString("gmailEmail", email);
            if(!personPhoto.equals(""))
                editor.putString("gmailPhoto", personPhoto);
            if(!coverURL.equals(""))
                editor.putString("gmailCover", coverURL);
            editor.putBoolean("signedIn", true);
            editor.apply();
            MainActivity.userSignedIn = true;

            Log.d("QUI", sP.getBoolean("gmail", false) + "");
            Log.d("QUI" , sP.getString("gmailEmail", "") + " " + sP.getString("gmailFName", "") + " " +
                    lName + " " + personId + " " + personPhoto + " " + coverURL);
        }

        Log.d("QUI", "firstTime is " + MainActivity.firstTime);
        if (sP.getBoolean("firstTime", false)) {
            MainActivity.firstTime = false;
            editor.putBoolean("firstTime", false).apply();


            Log.d("QUI", "G+ Setup");
            // Async profile photos?
            Intent i = new Intent(this, SignInSetup.class);
            startActivity(i);
            finish();
        } else if(getIntent().getBooleanExtra("signedOut", false)) {
            // Check for if logout was initiated from elsewhere
            Log.d("QUI", "Disconnecting");
            if(editor.putBoolean("signedIn", false).commit()) {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
            } else {
                editor.putBoolean("signedIn", false).apply();
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
            }

            MainActivity.userSignedIn = false;
            getIntent().putExtra("signedOut", false);//??
        } else if(!sP.getBoolean("firstTime", false) && sP.getBoolean("signedIn", false)) {
            MainActivity.userSignedIn = true;
            editor.putBoolean("signedIn", true).apply();

            Intent i = new Intent(this, MainActivity.class);
            Log.d("G+ onsessionState", "StartedMainActivity");
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("QUI", "In onConnectionFailed");
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = connectionResult;
            Log.d("QUI", "In onConnectionFailedSetResult");

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }


    // Sign in process


    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        Log.d("QUI", "In resolve");
        if (mConnectionResult.hasResolution()) {
            Log.d("QUI", "In inIf");
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    // Google+


    // -----FB
    //>
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
            onSessionStateChange(session, state, exception);
        }
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("QUI", "FB Logged in...");
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser graphUser, Response response) {
                    Log.d("QUI", graphUser.getInnerJSONObject().toString());
                    editor.putBoolean("facebook", true);
                    editor.putString("facebookId", graphUser.getId());
                    editor.putString("facebookFName", graphUser.getFirstName());
                    editor.putString("facebookLName", graphUser.getLastName());
                    if(graphUser.asMap().get("email") != null) {
                        editor.putString("facebookEmail", graphUser.asMap().get("email").toString());
                        Log.d("QUI", graphUser.asMap().get("email").toString());
                    }
                    else {
                        editor.putBoolean("facebookNoEmail", true);
                    }

                    Log.d("QUITESTING", graphUser.getInnerJSONObject().toString());
                    editor.putString("facebookPicture",  "https://graph.facebook.com/"+graphUser.getId()+"/picture");

                    if(graphUser.asMap().get("email") != null)
                        Log.d("QUI", graphUser.asMap().get("email").toString() + " " +
                                graphUser.getFirstName() + " " + graphUser.getLastName()
                                + " "  + graphUser.getId());
                    editor.putBoolean("signedIn", true);
                    MainActivity.userSignedIn = true;
                    editor.apply();

                    Log.d("QUI", "firstTime is " + MainActivity.firstTime);
                    if (sP.getBoolean("firstTime", false)) {
                        MainActivity.firstTime = false;
                        editor.putBoolean("firstTime", false).apply();

                        // Async profile photos?
                        Log.d("QUI", "FB Setup");
                        Intent i = new Intent(getApplicationContext(), SignInSetup.class);
                        startActivity(i);
                        finish();
                    } else if(sP.getBoolean("facebookSignOut",false)) {
                        //TODO: CHANGE CONDITION ; USE OTHER SharedPreferences
                        editor.putBoolean("facebookSignOut", false);
                        editor.apply();
                        session.closeAndClearTokenInformation();
                        editor.putBoolean("signedIn", false).apply();
                    } else if(!sP.getBoolean("firstTime", false) && sP.getBoolean("signedIn", false)) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        Log.d("Fb Onsessionstate", "StartedMainActivity");
                        startActivity(i);
                        finish();
                    }
                }
            }).executeAsync();
        } else if (state.isClosed()) {
            Log.i("QUI", "FB Logged out...");
            editor.putBoolean("signedIn", false).apply();
            MainActivity.userSignedIn = false;
        }
    }
    // FB


    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        uiHelper.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
