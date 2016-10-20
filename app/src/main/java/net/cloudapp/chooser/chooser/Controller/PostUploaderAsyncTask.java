package net.cloudapp.chooser.chooser.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.cloudapp.chooser.chooser.Controller.Callbacks.PostUploadCallback;
import net.cloudapp.chooser.chooser.Images.ImageUploader;
import net.cloudapp.chooser.chooser.Images.ImageUploaderAsyncTask;
import net.cloudapp.chooser.chooser.Images.ImageUploaderImpl;
import net.cloudapp.chooser.chooser.Images.UploadTask;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;

import bolts.Task;

public class PostUploaderAsyncTask extends AsyncTask<PostUploadTask, Void, Void> {
    private Context mContext;

    public PostUploaderAsyncTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(PostUploadTask... posts) {
        Log.d("Chooser", "Upload task started");
        PostUploadTask postUploadtask = posts[0];
        String image1Id = "", image2Id = "";
        String title = postUploadtask.title;
        String description1 = postUploadtask.description1;
        String description2 = postUploadtask.description1;
        ImageUploader uploader = new ImageUploaderImpl();
        image1Id = uploader.uploadImage(postUploadtask.image1Bitmap);
        image2Id = uploader.uploadImage(postUploadtask.image2Bitmap);
        String token = postUploadtask.token;
        Context context = postUploadtask.context;
        RestClient restClient = new RestClient();

        restClient.getService().addpost(
                token,
                title,
                image1Id,
                image2Id,
                description1,
                description2,
                new PostUploadCallback(context)
        );
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("Chooser", "Post upload complete");
        Toast.makeText(mContext, "Post Uploaded!", Toast.LENGTH_LONG);
    }
}
