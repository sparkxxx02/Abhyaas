package com.placements.abhyaas.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.placements.abhyaas.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class Intro_Signin extends AppCompatActivity {
    ViewPager viewPager;
    WormDotsIndicator dot;
    ViewAdapter viewAdapter;
    TextView btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slides);
        viewPager= findViewById(R.id.view_pager);
        dot= findViewById(R.id.dot);
        viewAdapter= new ViewAdapter(Intro_Signin.this);
        viewPager.setAdapter(viewAdapter);
        dot.setViewPager(viewPager);

        btn= findViewById(R.id.car_skip);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intro_Signin.this, PhoneAuthActivity.class));
            }
        });


    }

}
