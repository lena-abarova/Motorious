package com.speeddigital.motorious.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.speeddigital.motorious.R;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private TextView mTvName, mTvOtherData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        configSignup();
    }

    private void initViews() {
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvOtherData = (TextView) findViewById(R.id.tv_data);

        Button mBtnSignout = (Button) findViewById(R.id.btn_logout);
        mBtnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase sign out
                mAuth.signOut();

                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(MainActivity.this, SigninActivity.class));
                                MainActivity.this.finish();
                            }
                        });
            }
        });
    }


    private void configSignup() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mTvName.setText("Hello " + user.getDisplayName());
            mTvOtherData.setText("Email: " +user.getEmail()+"\n"+"Uid: "+user.getUid());
        }
    }
}