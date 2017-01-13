package net.cloudapp.chooser.chooser.Animations;

import android.view.animation.Animation;

import net.cloudapp.chooser.chooser.views.FeedView;

/**
 * Created by Ben on 18/10/2016.
 */
public class VoteAnimationListener implements Animation.AnimationListener {
    FeedView feed;
    boolean isOption1Selected;

    public VoteAnimationListener(FeedView feed, boolean isOption1Selected) {
        this.feed = feed;
        this.isOption1Selected = isOption1Selected;
        if (isOption1Selected)
            feed.animating1 = false;
        else
            feed.animating2 = false;
    }
    @Override
    public void onAnimationStart(Animation animation) {
        if (isOption1Selected)
            feed.animating1 = true;
        else
            feed.animating2 = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        if (isOption1Selected) {
            feed.onAnimationEnd1();
            feed.animating1 = false;
        } else {
            feed.onAnimationEnd2();
            feed.animating2 = false;
        }
    }
}