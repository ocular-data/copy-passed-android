package com.zivoy.copypassed;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class FireSignin {

    private static final int RC_SIGN_IN = 123;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private Activity activity;

    public FireSignin(){
    }

    public FireSignin(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        this.activity = activity;
        this.updateUser();
    }

    public FirebaseUser getUser() {
        updateUser();
        return user;
    }

    public void updateUser() {
        this.user = mAuth.getCurrentUser();
    }

    public void signin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            createSignInIntent();
        }
        this.updateUser();
    }

    public boolean authed() {
        return this.getUser() != null;
    }

    private void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.GitHubBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

        // Create and launch sign-in intent
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == Activity.RESULT_OK) {
//                // Successfully signed in
//                this.user = mAuth.getCurrentUser();
//                // ...
//            } else {
//                this.user = null;
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//            }
//        }
//    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        this.updateUser();
        // [END auth_fui_signout]
    }
}