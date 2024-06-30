package com.example.suitcase2;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.annotation.Annotation;

public class Splash_Screen extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView=findViewById(R.id.splash_img);

     animation= AnimationUtils.loadAnimation(this,R.anim.animation);
     imageView.setAnimation(animation);


     Thread thread=new Thread(new Runnable() {
         @Override
         public void run() {
         try {
             sleep(3000);
             Intent intent=new Intent(Splash_Screen.this, Signup_Page.class);
             startActivity(intent);
         }catch (InterruptedException e){
           e.printStackTrace();
         }

         }
     });
     thread.start();
    }
}