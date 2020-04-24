package com.zivoy.copypassed;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private FireSignin FireBaseAuth;
    private DatabaseReference mUserReference;

    private TextView mClipBoard;
    private TextView mUser;
    private ImageView mImg;

    private CharSequence dataText = "";
    private boolean listenerAttatched = false;
    private ValueEventListener dataUpdate = new ValueEventListener() {
        private static final String TAG = "FireListen";

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            CharSequence text = (CharSequence) dataSnapshot.getValue();
            dataText = text;
            mClipBoard.setText(text);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.FireBaseAuth = new FireSignin(this);
        mClipBoard = findViewById(R.id.clibBoard);
        mUser = findViewById(R.id.signedAs);
        mImg = findViewById(R.id.UserIcon);

//        Intent intent = new Intent(this, signin.class);
//        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.FireBaseAuth.signin();
        FireBaseAuth.mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUser();
                if (!listenerAttatched) if (FireBaseAuth.authed()) dataBaseListener();
            }
        });
        updateUser();

        if (!listenerAttatched) if (FireBaseAuth.authed()) dataBaseListener();

        mClipBoard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(dataText.toString()))
                    mClipBoard.getBackground().setColorFilter(getColor(R.color.black),
                            PorterDuff.Mode.SRC_ATOP);
                else mClipBoard.getBackground().setColorFilter(getColor(R.color.change),
                        PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    public void dataBaseListener() {
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FireBaseAuth.getUser().getUid())
                .child("last");
        mUserReference.addValueEventListener(dataUpdate);
        listenerAttatched = true;
    }

    public void updateUser() {
        if (FireBaseAuth.authed()) {
            mUser.setVisibility(View.VISIBLE);
            findViewById(R.id.loggedOut).setVisibility(View.GONE);

            mUser.setText(getString(R.string.SignedAs, FireBaseAuth.user.getEmail()));
            Picasso.get().load(FireBaseAuth.user.getPhotoUrl()).into(mImg);
            mImg.setVisibility(View.VISIBLE);
            findViewById(R.id.LogIn).setVisibility(View.GONE);
            findViewById(R.id.LogOut).setVisibility(View.VISIBLE);
        } else {
            mUser.setVisibility(View.GONE);
            findViewById(R.id.loggedOut).setVisibility(View.VISIBLE);
            mClipBoard.setText("");

            mImg.setVisibility(View.GONE);
            findViewById(R.id.LogOut).setVisibility(View.GONE);
            findViewById(R.id.LogIn).setVisibility(View.VISIBLE);
        }
//        mUser.invalidate();
//        mUser.requestLayout();
//        recreate();
    }

    public void LogOut(View view) {
//        this.FireBaseAuth.updateUser();
//        if (this.FireBaseAuth.user != null) {
//            this.FireBaseAuth.signOut();
//        }
//        this.FireBaseAuth.signin();
//        updateUser();
        this.FireBaseAuth.signOut();
        if (listenerAttatched) {
            mUserReference.removeEventListener(dataUpdate);
            listenerAttatched = false;
        }
        updateUser();
        dataText = "";
    }

    public void LogIn(View view) {
        this.FireBaseAuth.signin();
        updateUser();
        if (!listenerAttatched) if (FireBaseAuth.authed()) dataBaseListener();
    }
}
