package net.cloudapp.chooser.chooser.PostUpload;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by t-baya on 10/4/2016.
 */

public class PostUploadCallback implements Callback {
    private Context mContext;

    public PostUploadCallback(Context context){
        mContext = context;
    }
    @Override
    public void success(Object o, Response response) {
        Toast.makeText(mContext, "Post uploaded!", Toast.LENGTH_SHORT);
        Log.d("Chooser", "Post uploaded!");
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("Chooser", "Post upload failed");
        error.printStackTrace();
    }
}
