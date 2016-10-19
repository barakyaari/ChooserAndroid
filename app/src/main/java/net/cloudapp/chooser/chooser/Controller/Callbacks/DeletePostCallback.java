package net.cloudapp.chooser.chooser.Controller.Callbacks;

import android.widget.Toast;

import net.cloudapp.chooser.chooser.Controller.MyPostsFetchController;
import net.cloudapp.chooser.chooser.views.Statistics.StatisticsView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeletePostCallback implements Callback<Void> {
    StatisticsView view;
    public DeletePostCallback (StatisticsView view) {
        this.view = view;
    }

    @Override
    public void success(Void aVoid, Response response) {
        if (response.getStatus() == 200) {
            Toast.makeText(view, "Post Deleted!", Toast.LENGTH_LONG).show();
            view.finish();
        }
    }

    @Override
    public void failure(RetrofitError error) {

     }
}
