package net.cloudapp.chooser.chooser.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import net.cloudapp.chooser.chooser.Controller.DeletePostController;
import net.cloudapp.chooser.chooser.views.MyPosts.MyPostsView;


/**
 * Created by Ben on 18/10/2016.
 */
public class DeletePostDialog extends DialogFragment implements DialogInterface.OnClickListener {
    String post_id;
    MyPostsView myPostsView;

    public DeletePostDialog(String post_id, MyPostsView view) {
        this.post_id = post_id;
        myPostsView = view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this post?");
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);
        Log.d("Chooser", "Delete post dialog loaded");
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Log.d("Chooser", "Deletes post with id: " + post_id);
                DeletePostController dpController = new DeletePostController();
                dpController.deletePost(post_id, myPostsView);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }
}