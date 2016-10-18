package net.cloudapp.chooser.chooser.Animations.VotesAnimationListeners;

import android.view.animation.Animation;

import net.cloudapp.chooser.chooser.views.FeedView;

/**
 * Created by Ben on 18/10/2016.
 */
public class Vote2AnimationListener implements Animation.AnimationListener {
    FeedView feed;

    public Vote2AnimationListener(FeedView feed) {
        this.feed = feed;
        feed.animating2 = false;
    }
    @Override
    public void onAnimationStart(Animation animation) {
        feed.animating2 = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        feed.onAnimationEnd2();
        feed.animating2 = false;
    }

}