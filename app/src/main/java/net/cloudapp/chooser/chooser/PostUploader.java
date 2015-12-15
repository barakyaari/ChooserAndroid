package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PostUploader extends AsyncTask<Void, Void, Void> {
    private Bitmap bitmap;
    private String name;
    private String title, description1, description2, image1, image2;

    public PostUploader(String title, String image1, String description1, String image2, String description2){
        this.title = title;
        this.image1 = image1;
        this.description1 = description1;
        this.image2 = image2;
        this.description2 = description2;
    }

    @Override
    protected Void doInBackground(Void... params) {
        sendPost();
        return null;
    }

    private void sendPost() {
        String urlString = "http://localhost:8080/login";
        String charset = "UTF-8";
        try {


            //Open HttpUrlConnection:
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url + "?" + "connection").openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");
            urlConnection.setReadTimeout(35000);
            urlConnection.setConnectTimeout(35000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", charset);
            OutputStream os = urlConnection.getOutputStream();

            //Create JSON:
            JSONObject json = new JSONObject();
            json.put("title", title);
            json.put("image1", image1);
            json.put("image2", image2);
            json.put("description1", description1);
            json.put("description2", description2);

            byte[] postData = json.toString().getBytes();
            os.write(postData);
            os.flush();
            os.close();

            System.out.println("Response Code: " + urlConnection.getResponseCode());

            //Send:
            InputStream response = urlConnection.getInputStream();

            String responseText = response.toString();
            Log.i("ChooserApp", responseText);
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
