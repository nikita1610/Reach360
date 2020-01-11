package com.reach360.reach360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,View.OnClickListener , ViewPagerEx.OnPageChangeListener{

    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;
    Button begin1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        begin1 = (Button) findViewById(R.id.begin);
        begin1.setOnClickListener(this);


        Hash_file_maps = new HashMap<String, String>();

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        Hash_file_maps.put("Crime Alert", "http://www.safewise.com/blog/wp-content/uploads/Crime-Watch-600x315.png");
        Hash_file_maps.put("Best Safety Solution", "https://www.nottinghamshire.police.uk/sites/default/files/styles/police_large/public/PERSONWebsiteSlider.jpg?itok=Crl-rXOW");
        Hash_file_maps.put("Motion Detection", "https://lh3.googleusercontent.com/nBL_ugu48-xblwlljWqSAv_4VTPYSUM7T33pRviIU5jE5WEf1oNNM5RpaNf7U8HKgh4=h900-rw");
        Hash_file_maps.put("Shake Alert", "https://image.slidesharecdn.com/presentationsosapp-130806055912-phpapp02/95/personal-safety-app-for-women-1-638.jpg?cb=1375772050");
        Hash_file_maps.put("Reach 360", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQti0idLPhZMgbvSfXWnLpX2eQrLyI_HxYQwOQKJsHPdK00TCrm");
        Hash_file_maps.put("OTP Verification", "https://d13yacurqjgara.cloudfront.net/users/786490/screenshots/2767531/ezgif.com-gif-maker-2.gif");
        Hash_file_maps.put("Check Battery Levels", "https://laughingsquid.com/wp-content/uploads/2016/05/Battery-Share-Friend-Levels.jpg");
        for(String name : Hash_file_maps.keySet()){

            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);
    }
    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.begin:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;

            default:
                break;
        }

    }
}