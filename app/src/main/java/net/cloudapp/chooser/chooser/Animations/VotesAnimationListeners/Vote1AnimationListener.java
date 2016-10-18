package net.cloudapp.chooser.chooser.Animations.VotesAnimationListeners;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import net.cloudapp.chooser.chooser.model.Post;
import net.cloudapp.chooser.chooser.views.FeedView;

/**
 * Created by Ben on 18/10/2016.
 */
public class Vote1AnimationListener implements Animation.AnimationListener {
    FeedView feed;

    public Vote1AnimationListener(FeedView feed) {
        this.feed = feed;
        feed.animating1 = false;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        feed.animating1 = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        feed.onAnimationEnd1();
        feed.animating1 = false;
    }

}
