package net.cloudapp.chooser.chooser.Views.Animations;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ViewSwitcher;

import net.cloudapp.chooser.chooser.Views.Feed;

public class TextSwitchFactory implements ViewSwitcher.ViewFactory {
    private Context mContext;

    public TextSwitchFactory(Context context){
        mContext = context;
    }
    @Override
    public View makeView() {
        TextWithStroke text = new TextWithStroke(mContext);
        text.setTypeface(null, Typeface.BOLD);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(50);
        ImageSwitcher.LayoutParams lp = new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        text.setLayoutParams(lp);
        text.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/CooperBlackStd.otf"));
        text.setTextColor(Color.parseColor("#dddddd"));  //Light gray
        text.setStrokeSize(15);
        text.setStrokeColor(Color.BLACK);
        return text;
    }
}
