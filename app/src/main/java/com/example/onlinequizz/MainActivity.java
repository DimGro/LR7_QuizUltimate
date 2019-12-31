package com.example.onlinequizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlinequizz.Common.Common;
import com.example.onlinequizz.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    MaterialEditText editNewUser, editNewPassword, editNewEmaile; //sign up
    MaterialEditText editUser, editPassword; //sign in

    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(editUser.getText().toString(), editPassword.getText().toString());
            }
        });

    }

    private void SignIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if(login.getPassword().equals(pwd)){
                            Common.currentUser = login;
                            Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            startActivity(homeActivity);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(MainActivity.this,"Enter your user name", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this,"User is not exists", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        editNewUser = sign_up_layout.findViewById(R.id.editNewUserName);
        editNewPassword = sign_up_layout.findViewById(R.id.editNewPassword);
        editNewEmaile = sign_up_layout.findViewById(R.id.editNewEmaile);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(editNewUser.getText().toString(),
                                     editNewPassword.getText().toString(),
                                     editNewEmaile.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this,"User already exists!", Toast.LENGTH_SHORT).show();
                        else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this,"Registration success!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
