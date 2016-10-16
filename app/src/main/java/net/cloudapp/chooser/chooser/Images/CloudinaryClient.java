package net.cloudapp.chooser.chooser.Images;

import android.util.Log;

import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryClient {
    private static final String CLOUD_NAME = "chooser";


    public static void uploadImage(final InputStream inputStream, String imageId) {
        Log.d("Chooser", "Cloudinary uploading image");
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", "291124344125714");
        config.put("api_secret", "arRkoKCCGBPwnxXmMkZXi9q9TrU");
        config.put("public_id",imageId);
        Cloudinary cloudinary = new Cloudinary(config);
        try {
            cloudinary.uploader().upload(inputStream, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String bigImageUrl(String imageId){
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);

        Cloudinary cloudinary = new Cloudinary(config);

        return cloudinary.url().generate(imageId);
    }
}
