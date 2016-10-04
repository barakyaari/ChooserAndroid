package net.cloudapp.chooser.chooser.Animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by t-baya on 10/4/2016.
 */

public class TextWithStroke extends TextView {
    public TextWithStroke(Context context) {
        super(context);
    }

    private float strokeWidth;
    private int strokeColor;

    @Override
    protected void onDraw(Canvas pCanvas) {
        int textColor = getTextColors().getDefaultColor();
        setTextColor(strokeColor);
        getPaint().setStrokeWidth(strokeWidth);
        getPaint().setStyle(Paint.Style.STROKE);
        super.onDraw(pCanvas);
        setTextColor(textColor);
        getPaint().setStrokeWidth(0);
        getPaint().setStyle(Paint.Style.FILL);
        super.onDraw(pCanvas);
    }

    public void setStrokeSize(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setStrokeColor(float strokeColor) {
        this.strokeColor = (int) strokeColor;
    }

}
