package net.cloudapp.chooser.chooser.Controller;

import android.content.Context;
import android.graphics.Bitmap;

import net.cloudapp.chooser.chooser.Common.SessionDetails;

public class PostsUploadController {

    public void uploadPost(Context context, String title, Bitmap image1BitMap, Bitmap image2BitMap, String description1, String description2) {
        uploadPost(context, title, image1BitMap, image2BitMap, description1, description2, false);
    }

    public void uploadPost(Context context, String title, Bitmap image1BitMap, Bitmap image2BitMap, String description1, String description2, boolean notMyPost){
        String token = notMyPost? "NotMyPost" : SessionDetails.getInstance().getAccessToken().getToken();
        PostUploadTask uploadTask = new PostUploadTask(title, description1, description2, image1BitMap, image2BitMap, token, context);
        PostUploaderAsyncTask uploaderAsync = new PostUploaderAsyncTask(context);
        uploaderAsync.execute(uploadTask);
    }
}
