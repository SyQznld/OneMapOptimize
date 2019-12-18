package com.oneMap.module.common.widget.seekbar;

import android.content.Context;
import android.util.AttributeSet;



public class VerticalSeekBar extends VerticalBar {

    public VerticalSeekBar(Context context) {
        super(context);
        init();
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        showThumb(true);
        setTouchListenerEnabled(true);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener){
        super.setOnSeekBarChangeListener(listener);
    }
}