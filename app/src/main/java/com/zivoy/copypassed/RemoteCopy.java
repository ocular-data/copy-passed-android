package com.zivoy.copypassed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemoteCopy extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent();
        finish();
    }

    protected void handleIntent() {
        //setContentView(R.layout.process_text_main);
        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);


        FireSignin fireTime = new FireSignin(this);
        if (fireTime.getUser() == null) fireTime.signin();
        if (fireTime.getUser() == null) return;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users")
                .child(fireTime.user.getUid())
                .child("last")
                .setValue(text);

        Context context = getApplicationContext();
        CharSequence toastText = "Copied: " + text;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();
    }
}
