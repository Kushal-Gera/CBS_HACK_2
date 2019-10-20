package com.example.cbshack;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    long cu, cene;
    int counter = 1;

    TextView signOut;
    FloatingActionButton fab, fab1, fab2, fab3;
    boolean hidden = true;

    FirebaseDatabase database;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("shared_pref", MODE_PRIVATE);
        boolean is_prev = preferences.getBoolean("is_prev", false);
        if (!is_prev) {
            startActivity(new Intent(this, Details.class));
        }


        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

        fab3.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab1.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(v -> {

            if (hidden) {
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);

                fab1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
                fab2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
                fab3.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));

            }
            else {
                fab1.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.INVISIBLE);
                fab3.setVisibility(View.INVISIBLE);
            }
            hidden = !hidden;

        });

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getUid());

        fab1.setOnClickListener(v -> {
            //qr activity
            Intent i = new Intent(MainActivity.this, CameraActivity.class);
            i.putExtra("IS_QR", true);
            startActivity(i);
        });
        fab2.setOnClickListener(v -> {
            //qr activity
            Intent i = new Intent(MainActivity.this, CameraActivity.class);
            i.putExtra("IS_QR", false);
            startActivity(i);
        });
        fab3.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginAct.class));
            finish();
        });

        ConstraintLayout layout = findViewById(R.id.layout);
        layout.setOnClickListener(v -> {
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
        });


        ///////////////////////////////////////////////////////////////////////////////////////
        FirebaseAuth auth = FirebaseAuth.getInstance();

        options = new FirebaseRecyclerOptions.Builder<BMarkItems>()
                .setQuery(my_ref, BMarkItems.class).build();

        adapter = new FirebaseRecyclerAdapter<BMarkItems, Bm_viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Bm_viewholder holder, int i, @NonNull final BMarkItems items) {

                final String node_id = getRef(i).getKey();
                if (node_id == null) return;

                my_ref.child(node_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String link_url = String.valueOf(dataSnapshot.child(LINK).getValue());
                        final String image_url = String.valueOf(dataSnapshot.child(BM_IMAGE).getValue());
                        final String title = String.valueOf(dataSnapshot.child(TITLE).getValue());

                        holder.title.setText(title);
                        pd.dismiss();
                        Glide.with(holder.bm_image.getContext()).load(image_url)
                                .centerCrop().placeholder(R.drawable.placeholder).into(holder.bm_image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link_url)));
                            }
                        });

                        holder.cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                my_ref.child(node_id).removeValue();
                            }
                        });

                        holder.share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_TEXT, link_url);
                                startActivity(Intent.createChooser(i, "share via"));
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: Error");
                    }
                });
            }

            @NonNull
            @Override
            public Bm_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bk_mark_list, parent, false);
                return new Bm_viewholder(view);
            }


        };






    }

    void setAlarm(int hour){

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        startActivity(intent);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        startActivity(intent);
    }


}
