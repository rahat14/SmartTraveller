package com.metacoder.transalvania.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.metacoder.transalvania.R;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.channel.impl.ChannelManager;

import java.util.Collections;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        getSupportActionBar().hide();

//        PusherOptions options = new PusherOptions() ;
//        options.setCluster("mt1") ;
//
//        Pusher pusher = new Pusher("63c83050cea859d84b65", options);
//        pusher.connect();


       // pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", "Hello World"));

     //   Channel channeleds = pusher.subscribe("my-channel");

       // channel.trigger("my-channel", "my-event", Collections.singletonMap("message", "Hello World"));

//        channeleds.bind("test", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(PusherEvent event) {
//
//            }
//        });



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth mauth = FirebaseAuth.getInstance();
                FirebaseUser user = mauth.getCurrentUser();



                if (user == null) {
                    Intent p = new Intent(getApplicationContext(), WelcomeScreen.class);
                    startActivity(p);
                 //   finish();

                } else {
                    Intent p = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(p);
                    finish();
                }

            }
        } , 700) ;
    }


}