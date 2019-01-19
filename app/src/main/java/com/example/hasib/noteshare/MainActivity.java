package com.example.hasib.noteshare;


/**
 * This is the MainActivity from where the main app starts functioning
 * */


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasib.noteshare.model.Key;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    private final String CHANNEL_ID="private notification";
    private final int NOTIFICATION_ID=001;


    //SharedPreference handler class instance
    private UserSessionManager sessionManager;

    //Instances for navigation drawer
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBar actionBar;
    CardView cse,eee,bba,ce,eng,llb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cse=(CardView)findViewById(R.id.cseBoard);
        eee=(CardView)findViewById(R.id.eeeBoard);
        ce=(CardView)findViewById(R.id.civilBoard);
        bba=(CardView)findViewById(R.id.bbaBoard);
        eng=(CardView)findViewById(R.id.engBoard);
        llb=(CardView)findViewById(R.id.llbBoard);

        cse.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this,CSEDashBoard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });


        //Init. for navigation drawer
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Departments");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        //Checking if the user is already logged in, if not, finish the current activity
        sessionManager=new UserSessionManager(getApplicationContext());
        if(sessionManager.checkLogin()) finish();


        //Code for drawer and the components of the drawer
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_camera:{
                        break;
                    }
                    case R.id.nav_contact:{
                        break;
                    }
                    case R.id.nav_gallery :{
                        break;
                    }
                    case R.id.nav_signout:{
                        sessionManager.logout();
                        finish();
                        break;
                    }

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        displayNotification(sessionManager.getUserDetails().get(Key.USER_NAME));

    }

    //Overridden method onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void displayNotification(String username) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sign);
        builder.setContentTitle("NoteShare");
        builder.setContentText(username+" has just logged in");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

}
