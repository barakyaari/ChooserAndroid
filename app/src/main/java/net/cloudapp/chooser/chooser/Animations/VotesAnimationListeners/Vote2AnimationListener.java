package net.cloudapp.chooser.chooser.Animations.VotesAnimationListeners;

import android.view.animation.Animation;

import net.cloudapp.chooser.chooser.views.FeedView;

/**
 * Created by Ben on 18/10/2016.
 */
public class Vote2AnimationListener implements Animation.AnimationListener {
    boolean animating;
    FeedView feed;

    public Vote2AnimationListener(FeedView feed) {
        this.feed = feed;
        animating = false;
    }
    @Override
    public void onAnimationStart(Animation animation) {
        animating = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        feed.onAnimationEnd2();
        animating = false;
    }

}