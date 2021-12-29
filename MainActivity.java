package com.aws.socallogin;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button button, facebook;

    LoginButton facebookLoginBtn;

    GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button);
        facebook = findViewById(R.id.btn_facebook);
        facebookLoginBtn = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.setPermissions(Arrays.asList("email", "public_profile"));

        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String full_Name = profile.getName();
                    Log.e(TAG, "this is full name = "+full_Name);
                } else {
                    Log.e(TAG, "profile is null");
                }

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String profile_name = object.getString("name");
                            Log.e(TAG, "this is profile name = "+profile_name);
                            Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);

                            if(object.has("email")) {
                                String email = object.getString("email");
                                Log.e(TAG, "this is profile name = " + email);
                                resultIntent.putExtra("Email", email);
                            }

                            resultIntent.putExtra("Name", profile_name);
                            startActivity(resultIntent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "this is exception with facebook profile details = "+e.getMessage());
                        }
                    }
                });
                request.executeAsync();

                Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "User cancelled login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "facebook login error " + error.getMessage());
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLoginBtn.performClick();
            }
        });


    }

    int RC_SIGN_IN = 75;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e(TAG, "firebaseAuthWithGoogle: google login token = " + account.getIdToken());

                String email = account.getEmail().toString();
                String name = account.getDisplayName().toString();
//                String pic = account.getPhotoUrl().toString();
                Intent resultActivityIntent = new Intent(MainActivity.this, ResultActivity.class);
                resultActivityIntent.putExtra("Email", email);
                resultActivityIntent.putExtra("Name", name);
//                resultActivityIntent.putExtra("image",pic);

                startActivity(resultActivityIntent);

//                Toast.makeText(MainActivity.this, account.getEmail(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, account.getDisplayName(), Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }


    }

}