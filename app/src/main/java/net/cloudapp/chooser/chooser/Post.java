package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Barak on 29/11/2015.
 */
public class Post {
    public String title;
    public String description1;
    public String description2;
    public GregorianCalendar date;
    public int promotionMinutes;
    public String id;
    public Bitmap image1;
    public Bitmap image2;
    public int votes1;
    public int votes2;
    public PostStatistics postStatistics;

    public Post(String title, Bitmap image1, String description1, Bitmap image2, String description2, String id, int votes1, int votes2) {
        Log.i("ChooserApp", "Creating New Post: " + title);
        this.title = title;
        this.description1 = description1;
        this.description2 = description2;
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
        this.votes1 = votes1;
        this.votes2 = votes2;
        postStatistics = new PostStatistics();
    }

    public boolean isPromoted () {
        return promotionMinutes == -1;
    }

    public void addVote(int picNum) {
        switch (picNum) {
            case 1:
                votes1++;
                break;
            case 2:
                votes2++;
                break;
        }
    }

    public int getPercentage(int picNum) {
        double sum = votes1 + votes2;
        int vote1 = (int)Math.round(votes1*100/sum);
        if (sum == 0)
            return 0;
        switch (picNum) {
            case 1:
                return vote1;
            case 2:
                return 100-vote1;
        }
        return -1;
    }

    public void setDate (String stringDate) {
        // "yyyy.mm.dd hh:mm:ss"
        date = string2Calendar(stringDate);
    }

    public static GregorianCalendar string2Calendar (String stringDate) {
        int year = Integer.valueOf(stringDate.substring(0,4));
        int month = Integer.valueOf(stringDate.substring(5,7));
        int day = Integer.valueOf(stringDate.substring(8,10));
        int hour = Integer.valueOf(stringDate.substring(11,13));
        int minute = Integer.valueOf(stringDate.substring(14,16));
        int second = Integer.valueOf(stringDate.substring(17,19));
        return new GregorianCalendar(year,month,day,hour,minute,second);
    }

    public String getShortDate () {
        return date.get(Calendar.DAY_OF_MONTH) + "." +date.get(Calendar.MONTH) + "." + date.get(Calendar.YEAR);
    }

    public static String bitmap2String(Bitmap bitmap, int width, int height) {
        if (width == 0)
            width = bitmap.getWidth();
        if (height == 0)
            height = bitmap.getHeight();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        smallBitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        smallBitmap.recycle();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap string2Bitmap(String str) {
        byte[] decodedString = Base64.decode(str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


}
