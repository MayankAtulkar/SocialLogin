package zyloon.main.com.sociallogin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends FragmentActivity {

    public LoginButton loginButton;
    private TextView info;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Message: ", "Welcome1 ");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        Log.d("Message: ", "Welcome2 ");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "zyloon.main.com.sociallogin",
                    PackageManager.GET_SIGNATURES);
            Log.d("Message: ", "Welcome33 ");

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Message Key: ", "Welcome ");

                Log.d("Message KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {


        } catch (NoSuchAlgorithmException e) {

        }
        loginButton = (LoginButton)findViewById(R.id.login_button);
        info = (TextView)findViewById(R.id.info);
        Log.d("Message: ", "Welcome3 ");

        loginButton.setReadPermissions(Arrays.asList("user_status"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("Message: ", "Welcome4 ");

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                Log.d("Fb Login Success ", "In OnSuccess");
                if(profile != null){
                    Log.d("Message: ", "Welcome "+profile.getName() +"\t"+ profile.getFirstName() +"\t"+ profile.getLastName());
                }
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + profile.getFirstName()
                );

            }

            @Override
            public void onCancel() {

                Log.d("APP", "onCancel");
                Toast.makeText(getApplication(), "login via Facebook cancelled by user ", Toast.LENGTH_LONG).show();
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {

                Log.d("APP", "onError");
                Toast.makeText(getApplication(), "Error Occurred!, Unable to login via Facebook, please try again ", Toast.LENGTH_LONG).show();
                info.setText("Login attempt failed.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}