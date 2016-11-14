package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import net.cloudapp.chooser.chooser.Controller.Callbacks.PromoteCallback;
import net.cloudapp.chooser.chooser.Controller.PromotionController;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsView;

/**
 * Created by user1 on 11/11/2016.
 */

public class PromotePostDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private String postId;
    private StatisticsView view;

    public PromotePostDialog(String postId, StatisticsView view) {
        this.postId = postId;
        this.view = view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Promote Post");
        builder.setMessage("Are you sure you want to promote this post for "
                + PromoteCallback.PROMOTION_COST + " tokens?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                PromotionController promotionController = new PromotionController();
                promotionController.promote(postId, view);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }
}
