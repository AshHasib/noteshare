package com.example.hasib.noteshare;

/**
 * This is the SignUp Activity for prompting user to give information to SignUp
 * */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hasib.noteshare.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    //View Components object instances
    private TextView txtAlreadyUser;
    private EditText txtFullName,txtEmail,txtPhone,txtPassword, txtRePassword;
    private Button btnSignUp;
    private ProgressBar progressBar;


    //Firebase Authentication object instance
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //View Component objects init.
        txtFullName=(EditText)findViewById(R.id.signUpFullName);
        txtEmail=(EditText)findViewById(R.id.signUpEmail);
        txtPhone=(EditText)findViewById(R.id.signUpPhone);
        txtPassword=(EditText)findViewById(R.id.signUpPassword);
        txtRePassword=(EditText)findViewById(R.id.signUpRePassword);
        progressBar=findViewById(R.id.progress);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v->{
            signUp();
        });

        txtAlreadyUser=findViewById(R.id.alreadyUserTxt);
        txtAlreadyUser.setOnClickListener(v->{
            startActivity(new Intent(SignUpActivity.this,LoginScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });

        //Auth object init
        auth=FirebaseAuth.getInstance();
    }

    //Signup method
    public void signUp()
    {
        String fullName=txtFullName.getText().toString();
        String email=txtEmail.getText().toString();
        String phone=txtPhone.getText().toString();
        String pass=txtPassword.getText().toString();
        String repass=txtRePassword.getText().toString();

        if(checkWithRegex(fullName,email,phone,pass,repass))
        {
            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {

                    //The registration is successful, so lets store the additional fields
                    User user=new User(fullName,phone,email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(task1 -> {
                                progressBar.setVisibility(View.GONE);
                                if(task1.isSuccessful()) {
                                    new AlertDialog.Builder(SignUpActivity.this)
                                            .setTitle("Welcome")
                                            .setMessage("Hello "+user.getName()+", please login to continue")
                                            .setInverseBackgroundForced(true)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(SignUpActivity.this,LoginScreen.class));
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"All information can't be stored",Toast.LENGTH_LONG).show();
                                    clearAll();
                                }
                    });
                    //Toast.makeText(getApplicationContext(),"User created",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            });

            //Toast.makeText(getApplicationContext(),"SignUp Successful",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Signup error",Toast.LENGTH_SHORT).show();
        }
    }


    /**
        checking input data using REGULAR EXPRESSION
     */
    public boolean checkWithRegex(String fN,String eml,String phn, String pass,String repass)
    {
        //empty check
        if(TextUtils.isEmpty(fN) || TextUtils.isEmpty(eml) || TextUtils.isEmpty(phn)||
                TextUtils.isEmpty(pass)||TextUtils.isEmpty(repass))
        {
            return false;
        }


        //checking email
        String emailPattern="^[a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-z]{2,3}";
        Pattern pt;
        pt=Pattern.compile(emailPattern);
        Matcher mt;
        mt=pt.matcher(eml);
        if(!mt.matches())
        {
            Toast.makeText(getApplicationContext(),"Enter a valid email",Toast.LENGTH_SHORT).show();
            return false;
        }

        //Checking phone number
        String phonePattern="\\+?(88)?0?1[56789][0-9]{8}\\b";
        pt=Pattern.compile(phonePattern);
        mt=pt.matcher(phn);
        if(!mt.matches())
        {
            Toast.makeText(getApplicationContext(),"Please enter a valid phone number",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!pass.equals(repass))
        {
            Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void clearAll(){
        txtFullName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtPassword.setText("");
        txtRePassword.setText("");
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser()!=null){
            //The user is already logged in
        }
        else {
            //No user is logged in
        }
    }
}
