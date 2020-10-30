package com.sxw.baseui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sxw.baseui.view.imageview.PinchImageView;

public class MainActivity extends AppCompatActivity {
    private PinchImageView mPinchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPinchImageView = findViewById(R.id.iv_pinch);
        mPinchImageView.setImageResource(R.mipmap.icon_rb_soundking);
    }
}
