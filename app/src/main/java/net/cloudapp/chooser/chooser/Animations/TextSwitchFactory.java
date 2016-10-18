package net.cloudapp.chooser.chooser.Animations;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ViewSwitcher;

import net.cloudapp.chooser.chooser.Animations.VotesAnimationListeners.Vote1AnimationListener;
import net.cloudapp.chooser.chooser.Animations.VotesAnimationListeners.Vote2AnimationListener;
import net.cloudapp.chooser.chooser.views.FeedView;

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

    public static Animation getAnimation(int postNum, FeedView feed) {
        Animation inAnimation = AnimationUtils.loadAnimation(feed, android.R.anim.fade_in);
        inAnimation.setDuration(1000);
        if (postNum == 1)
            inAnimation.setAnimationListener(new Vote1AnimationListener(feed));
        else
            inAnimation.setAnimationListener(new Vote2AnimationListener(feed));
        return inAnimation;
    }
}
