package com.oneMap.module.common.widget.seekbar;

/**
 *
 */

public interface OnSeekBarChangeListener {
    void onStartTrackingTouch(double progressInPercent);
    void onStopTrackingTouch(double progressInPercent);
    void onProgressChange(double progressInPercent);
}
