package com.sxw.baseui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sxw.baseui.R;


/**
 * 作者：sxw on 2019/9/11/011 11:17
 */
public class SimpleShowPopView extends LinearLayout {

    private TextView tvName;
    private ImageView ivArrow;

    public SimpleShowPopView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleShowPopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleShowPopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        LayoutInflater.from(context).inflate(R.layout.simplepop_viewlayout, this, true);

        tvName = findViewById(R.id.tv_name);
        ivArrow = findViewById(R.id.iv_arrow);
    }

    public void setNameText(String text) {
        tvName.setText(text);
    }

    public void setRightImageView(int ivResId) {
        ivArrow.setImageResource(ivResId);
    }

    public void isPop(boolean isPop) {
        if (isPop) {
            ivArrow.animate().rotation(180).setDuration(300).start();
        } else {
            ivArrow.animate().rotation(0).setDuration(300).start();
        }
    }

    public void setRightImageViewVisiable(int visiable){
        ivArrow.setVisibility(visiable);
    }
    public void setTextViewStyleLeftCenter(){
        tvName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvName.getLayoutParams();
        layoutParams.weight = 1;
        tvName.requestLayout();
    }

}
