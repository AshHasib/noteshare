package com.example.hasib.noteshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.hasib.noteshare.model.Key;

import java.util.HashMap;

public class UserSessionManager {


    //Instances declaration
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;



    //Constructor
    public UserSessionManager(Context context) {
        this.context=context;
        pref=context.getSharedPreferences(Key.PREF_NAME,0); // 0 for private mode
        editor=pref.edit();
    }


    //Create Session, write data on SharedPreference file
    public void createSession(String name,String phone,String email,String password) {
        editor.putBoolean(Key.IS_USER_LOGGED_IN,true);
        editor.putString(Key.USER_NAME,name);
        editor.putString(Key.USER_PHONE,phone);
        editor.putString(Key.USER_EMAIL,email);
        editor.putString(Key.USER_PASS,password);

        editor.commit();
    }


    //If not logged in, turn the Login Activity on, else return false so that the MainActivity remains visible
    public boolean checkLogin() {
        if(!this.isUserLoggedIn()) {
            Intent i=new Intent(context, LoginScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }




    //checks if the user is logged in
    public boolean isUserLoggedIn() {

        return pref.getBoolean(Key.IS_USER_LOGGED_IN,false);
    }


    //getting user information from the SharedPreference file
    public HashMap<String,String > getUserDetails() {
        HashMap<String, String> details=new HashMap<>();
        String name=pref.getString(Key.USER_NAME,null);
        String phone=pref.getString(Key.USER_PHONE,null);
        String email=pref.getString(Key.USER_EMAIL,"null");
        String pass=pref.getString(Key.USER_PASS,"null");
        details.put(Key.USER_NAME,name);
        details.put(Key.USER_PHONE,phone);
        details.put(Key.USER_EMAIL,email);
        details.put(Key.USER_PASS,pass);

        return details;
    }



    //Method for Logout
    public void logout(){
        editor.clear();
        editor.commit();
        Intent i=new Intent(context,LoginScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
