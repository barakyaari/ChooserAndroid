package net.cloudapp.chooser.chooser.Images;

import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryClient {
    private static final String CLOUD_NAME = "chooser";
    private static final int SMALL_IMAGE_SIZE_WIDTH = 133;
    private static final int SMALL_IMAGE_SIZE_HEIGHT = 100;
    private static final int BIG_IMAGE_BORDER_RADIUS = 7;
    private static final int SMALL_IMAGE_BORDER_RADIUS = 3;
    private static final String BORDER_COLOR = "solid_black";
    private static final String BIG_IMAGE_BORDER_THICKNESS = "3px";
    private static final String SMALL_IMAGE_BORDER_THICKNESS = "2px";





    public static String uploadImage(final InputStream inputStream) {
        Log.d("Chooser", "Cloudinary uploading image");
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", "291124344125714");
        config.put("api_secret", "arRkoKCCGBPwnxXmMkZXi9q9TrU");
        // config.put("public_id",imageId);
        // my new line
        Cloudinary cloudinary = new Cloudinary(config);
        Map result;
        try {
            result = cloudinary.uploader().upload(inputStream, config);
            Log.d("Chooser", "Image uploaded");
            return (String)result.get("public_id");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String bigImageUrl(String imageId, boolean border){
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);

        Cloudinary cloudinary = new Cloudinary(config);
        if (border)
            return cloudinary.url().transformation(new Transformation()
                    .border(BIG_IMAGE_BORDER_THICKNESS+"_"+BORDER_COLOR)
                    .radius(BIG_IMAGE_BORDER_RADIUS))
                    .generate(imageId);
        else
            return cloudinary.url().generate(imageId);
    }

    public static String smallImageUrl(String imageId, boolean border){
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);

        Cloudinary cloudinary = new Cloudinary(config);
        if (border)
            return cloudinary.url().transformation(new Transformation()
                    .width(SMALL_IMAGE_SIZE_WIDTH)
                    .height(SMALL_IMAGE_SIZE_HEIGHT)
                    .crop("thumb")
                    .border(SMALL_IMAGE_BORDER_THICKNESS+"_"+BORDER_COLOR)
                    .radius(SMALL_IMAGE_BORDER_RADIUS))
                    .generate(imageId);
        else
            return cloudinary.url().transformation(new Transformation()
                    .width(SMALL_IMAGE_SIZE_WIDTH)
                    .height(SMALL_IMAGE_SIZE_HEIGHT)
                    .crop("thumb"))
                    .generate(imageId);
    }
}
