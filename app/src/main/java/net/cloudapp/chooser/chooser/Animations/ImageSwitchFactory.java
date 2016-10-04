package net.cloudapp.chooser.chooser.Animations;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by t-baya on 10/4/2016.
 */

public class ImageSwitchFactory implements ViewSwitcher.ViewFactory {
    private Context mContext;

    public ImageSwitchFactory(Context context){
        mContext = context;
    }

    @Override
    public View makeView() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        ImageView image = new ImageView(getApplicationContext());
        image.setLayoutParams(lp);
        return image;
    }
}
