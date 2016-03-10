package com.liu.happygrow.colorUi.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.liu.happygrow.colorUi.ColorUiInterface;
import com.liu.happygrow.colorUi.util.ViewAttributeUtil;


/**
 * Created by chengli on 15/6/8.
 */
public class ColorFrameLayout extends NavigationView implements ColorUiInterface {

    private int attr_background = -1;

    public ColorFrameLayout(Context context) {
        super(context);
    }

    public ColorFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attr_background = ViewAttributeUtil.getBackgroundAttibute(attrs);
    }

    public ColorFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attr_background = ViewAttributeUtil.getBackgroundAttibute(attrs);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme themeId) {
        if(attr_background != -1) {
            ViewAttributeUtil.applyBackgroundDrawable(this, themeId, attr_background);
        }
    }
}
