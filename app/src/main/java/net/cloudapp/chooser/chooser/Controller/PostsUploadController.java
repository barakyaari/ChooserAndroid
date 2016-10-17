package net.cloudapp.chooser.chooser.Controller;

import android.content.Context;
import android.graphics.Bitmap;

import net.cloudapp.chooser.chooser.Common.SessionDetails;
import net.cloudapp.chooser.chooser.Images.ImageUploader;
import net.cloudapp.chooser.chooser.Images.ImageUploaderImpl;
import net.cloudapp.chooser.chooser.Network.RestFramework.RestClient;
import net.cloudapp.chooser.chooser.Controller.Callbacks.PostUploadCallback;

public class PostsUploadController {

    public void uploadPost(Context context, String title, Bitmap image1BitMap, Bitmap image2BitMap, String description1, String description2){
        ImageUploader uploader = new ImageUploaderImpl();
        String image1Id = "", image2Id = "";
        RestClient restClient = new RestClient();
        restClient.getService().addpost(
                SessionDetails.getInstance().getAccessToken().getToken(),
                title,
                image1Id,
                image2Id,
                description1,
                description2,
                new PostUploadCallback(context)
        );
    }
}
