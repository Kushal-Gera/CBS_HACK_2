package com.example.cbshack;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "TAG";
    long cu, cene;
    int counter = 1;
    static TextView txt ;

    TextView signOut;
    FloatingActionButton fab, fab1, fab2, fab3;
    boolean hidden = true;

    LottieAnimationView load;

    RecyclerView recycler_view;
    FirebaseAuth auth;
    DatabaseReference my_ref;
//    FirebaseRecyclerOptions<Each_item> options;
//    FirebaseRecyclerAdapter<Each_item, My_viewHolder> adapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt = findViewById(R.id.text_view) ;
        SharedPreferences preferences = getSharedPreferences("shared_pref", MODE_PRIVATE);
        boolean is_prev = preferences.getBoolean("is_prev", false);
        if (!is_prev) {
            startActivity(new Intent(this, Details.class));
        }


//        recycler_view = findViewById(R.id.recycler_view);

        load = findViewById(R.id.load);
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
        my_ref = FirebaseDatabase.getInstance().getReference().child("Patient")
                .child(auth.getCurrentUser().getUid()).child("Medicines");


//        options = new FirebaseRecyclerOptions.Builder<Each_item>()
//                .setQuery(my_ref, Each_item.class).build();
//
//        adapter = new FirebaseRecyclerAdapter<Each_item, My_viewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull final My_viewHolder holder, int i, @NonNull final Each_item items) {
//
//                final String node_id = getRef(i).getKey();
//                if (node_id == null) {
//                    return;
//                }
//                Toast.makeText(MainActivity.this, "hey"+ node_id, Toast.LENGTH_SHORT).show();
//                my_ref.child(node_id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        recycler_view.setVisibility(View.VISIBLE);
//                        load.setVisibility(View.INVISIBLE);
//
//                        holder.title.setText(dataSnapshot.getValue().toString());
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e(TAG, "onCancelled: Error");
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public My_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
//                return new My_viewHolder(view);
//            }
//
//
//
//
//        };
//
//        recycler_view.setLayoutManager(new LinearLayoutManager(this));
//        recycler_view.setAdapter(adapter);
//        adapter.startListening();


    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }

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
