package com.example.gpapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gpapplication.databinding.ActivityNewActivtyBinding;

public class NewActivty extends AppCompatActivity {
    TextView name,email;
    Button Logout;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activty);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        Logout= findViewById(R.id.Logout_button);
        Logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Logout();
            }
        });
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            String Name= account.getDisplayName();
            String Email=account.getEmail();
            name.setText(Name);
            email.setText(Email);
        }
    }
   /* public void Logout(){
        gsc.signOut().addOnCompleteListener(new onCompleteListener<Void>(){
            @Override
                    public void onComplete(@NonNull Task<Void> task){
                finish();
                startActivity(new Intent(NewActivty.this,MainActivity.class));
            }

        });
    }
    */
}