package com.aws.socallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

public class ResultActivity extends AppCompatActivity {

    TextView Email, Name;
    Button SignOut;
    ImageView imageView;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Email = findViewById(R.id.showEmail);
        Name = findViewById(R.id.showName);
        SignOut = findViewById(R.id.btnSign);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");
        String name = intent.getStringExtra("Name");
//        String image= intent.getStringExtra("image");


        Email.setText(email);
        Name.setText(name);
//        Picasso.get().load(image).resize(100,100).into(imageView);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}