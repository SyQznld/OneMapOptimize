package com.oneMap.module.common.bean;

import android.view.View;

import com.oneMap.module.common.widget.materailintroview.shape.Focus;

public class ModuleData {
    private View moduleView;
    private String id;
    private String text;
    private Focus focus;

    public View getModuleView() {
        return moduleView;
    }

    public void setModuleView(View moduleView) {
        this.moduleView = moduleView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Focus getFocus() {
        return focus;
    }

    public void setFocus(Focus focus) {
        this.focus = focus;
    }
}
