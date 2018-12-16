package com.example.hasib.noteshare;

//
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.hasib.noteshare.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {
    private FirebaseAuth auth;
    private String email,password;

    private EditText getTxtEmail;
    private EditText getTxtPass;
    private Button loginBtn,signupBtn;
    private ProgressBar progressBar;


    UserSessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        sessionManager=new UserSessionManager(getApplicationContext());

        //FirebaseAuth object init.
        auth=FirebaseAuth.getInstance();

        getTxtEmail=(EditText)findViewById(R.id.loginEmail);
        getTxtPass=(EditText)findViewById(R.id.loginPassword);
        progressBar=findViewById(R.id.loginProgress);
        loginBtn=(Button) findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(v-> {
            login();
        });

        signupBtn=(Button) findViewById(R.id.signUpButton);
        signupBtn.setOnClickListener(v-> {
            showSignUp();
        });
    }


    public void login() {
        progressBar.setVisibility(View.VISIBLE);
        email=getTxtEmail.getText().toString().trim();
        password=getTxtPass.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference("Users").
                            addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data:dataSnapshot.getChildren()){
                                        if(data.child("email").getValue().equals(email)){
                                            progressBar.setVisibility(View.GONE);
                                            User user=data.getValue(User.class);
                                            sessionManager.createSession(user.getName(),user.getPhoneNumber(),email,password);
                                            //Show AlertDialog
                                            new AlertDialog.Builder(LoginScreen.this)
                                                    .setTitle("Login Message")
                                                    .setMessage("Hello "+user.getName()+", please click Ok to continue")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    progressBar.setVisibility(View.GONE);
                                    new AlertDialog.Builder(LoginScreen.this)
                                            .setTitle("Login Message")
                                            .setMessage("An Error occurred")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    clearAll();
                                                }
                                            })
                                            .show();
                                }
                            });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(LoginScreen.this)
                            .setTitle("Login Message")
                            .setMessage("No such user found")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clearAll();
                                }
                            })
                            .show();
                    return;
                }
            });


        }
        else {
            Toast.makeText(getApplicationContext(),"Please fill up properly",Toast.LENGTH_LONG).show();
        }
    }

    public void showSignUp() {
        Intent i=new Intent(getApplicationContext(),SignUpActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    public void clearAll(){
        getTxtEmail.setText("");
        getTxtPass.setText("");
    }

}
