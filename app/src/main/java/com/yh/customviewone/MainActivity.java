package com.yh.customviewone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CirclePercentView circlePercentView = (CirclePercentView) findViewById(R.id.circleView);
        circlePercentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float percent = (float) (Math.random() * 99 + 1);
                circlePercentView.setCurPercent(percent);
            }
        });
    }
}
