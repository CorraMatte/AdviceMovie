package com.example.corra.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginButton btnLogin;
    private TextView txtWelcome;
    private CallbackManager callbackManager;
    final LoginActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (LoginButton) findViewById(R.id.btnLogin);
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);

        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        if (loggedIn){
            //openApp();
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_friends"));
        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                openApp();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void openApp(){
        startActivity(new Intent(context, MainActivity.class));
    }
}


