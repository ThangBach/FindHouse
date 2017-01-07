package com.example.thangbach.findhouse.VIEW;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.User;
import com.example.thangbach.findhouse.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    TextView    txt_dangky;

    EditText    edt_username,
                edt_password;

    ImageButton btn_google,
                btn_twitter;

    Button      btn_login;

    LoginButton btn_facebook;

    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    //Google
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    //facebook
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();

        anhXa();
        btn_google.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_twitter.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance();


        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Toast.makeText(LoginActivity.this,"onAuthStateChanged:signed_in:" + user.getUid(), Toast.LENGTH_SHORT).show();
//                    User userCurrent=new User(user.getUid(),
//                                                user.getProviderId(),
//                                                user.getDisplayName(),
//                                                "",
//                                                user.getPhotoUrl().toString(),
//                                                user.getEmail());
//                    Toast.makeText(LoginActivity.this, ""+userCurrent.getUrlImg(), Toast.LENGTH_SHORT).show();
                    goHome("FIREBASE");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        // Initialize Facebook Login button

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edt_username.getText().toString(),edt_password.getText().toString());
            }
        });

        btn_facebook.setReadPermissions("email", "public_profile");
        btn_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                goHome("FB");
                Toast.makeText(LoginActivity.this, ""+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        // [END initialize_fblogin]

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // [START auth_with_facebook]
    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Authentication Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
// [END auth_with_facebook]

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Toast.makeText(this, "firebaseAuthWithGoogle"+acct.getId(), Toast.LENGTH_SHORT).show();
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Toast.makeText(LoginActivity.this, "onComplete: "+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    private void anhXa(){
        txt_dangky=(TextView)findViewById(R.id.lg_txt_dangky);

        edt_username=(EditText)findViewById(R.id.lg_edt_username);
        edt_password=(EditText)findViewById(R.id.lg_edt_password);

        btn_login=(Button)findViewById(R.id.lg_btn_login);

        btn_google=(ImageButton)findViewById(R.id.lg_btn_login_google);
        btn_twitter=(ImageButton)findViewById(R.id.lg_btn_login_twitter);
        btn_facebook=(LoginButton)findViewById(R.id.lg_btn_facebook);
    }


    public void goSignUp(View v){
        Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Dăng nhập thành công !", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    public void goHome(String loginBy){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("USER",loginBy);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.lg_btn_login_google:{
                signIn();
            }
        }
    }
}
