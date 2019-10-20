package com.example.cbshack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    long cu, cene;
    int counter = 1;

    TextView signOut;
    FloatingActionButton fab, fab1, fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
                fab2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
            }
        });

        SharedPreferences preferences = getSharedPreferences("shared_pref", MODE_PRIVATE);
        boolean is_prev = preferences.getBoolean("is_prev", false);

        if (!is_prev) {
            startActivity(new Intent(this, Details.class));
        }

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getUid());

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //qr activity
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                i.putExtra("IS_QR", true);
                startActivity(i);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //qr activity
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                i.putExtra("IS_QR", false);
                startActivity(i);
            }
        });

//        findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Do you really want to sign out ?")
//                        .setCancelable(true)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                FirebaseAuth.getInstance().signOut();
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });


        ConstraintLayout layout = findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cu = System.currentTimeMillis();
                if (counter == 5) {
                    startActivity(new Intent(getApplicationContext(), EmergencyActivity.class));
                    counter = 1;
                } else if (counter == 1 || cu < cene + 500) {
                    Log.e("Emergency Counter ", counter + " ");
                    counter++;
                } else {
                    counter = 1;
                }
                cene = cu;
            }
        });


    }
}
