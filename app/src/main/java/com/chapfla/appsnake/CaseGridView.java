package com.chapfla.appsnake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.ImageView;

public class CaseGridView {
    private Integer monImage;
    private int position;

    public CaseGridView(Integer monImage, int position) {
        this.monImage = monImage;
        this.position = position;
    }

    public int getMonImage() {
        return monImage;
    }

    public void setMonImage(Integer monImage) {
        this.monImage = monImage;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
