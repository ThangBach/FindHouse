package com.example.thangbach.findhouse.VIEW;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.thangbach.findhouse.R;

public class SplashActivity extends AppCompatActivity {

    ImageView spl_img;
    TextView    spl_title_1,
                spl_title_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        anhXa();
        visible();

        YoYo.with(Techniques.BounceIn)
                .duration(1000)
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        spl_img.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        YoYo.with(Techniques.DropOut)
                                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                                        spl_title_1.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                                    }
                                })
                                .duration(1000)
                                .playOn(findViewById(R.id.spl_title_1));
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                    }
                })
                .playOn(findViewById(R.id.spl_image));


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        },3000);
    }

    private void anhXa(){
        spl_img=(ImageView)findViewById(R.id.spl_image);
        spl_title_1=(TextView)findViewById(R.id.spl_title_1);
        spl_title_2=(TextView)findViewById(R.id.spl_title_2);
    }
    private void visible(){
        spl_img.setVisibility(View.INVISIBLE);
        spl_title_1.setVisibility(View.INVISIBLE);
    }
}
