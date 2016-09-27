package net.cloudapp.chooser.chooser;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Ben on 29/08/2016.
 */


public class NotifierBackgroundThread extends IntentService {
    private NotificationCompat.Builder notification;
    private static final int uniqueID = 20214001;
    private static int postID;
    private static long notificationValue;
    private static NotificationDialog.NotificationMethod method;
    private SessionDetails sessionDetails;
    private Post post;
    private int currentVoteCount;


    public NotifierBackgroundThread() {
        super("Notifier Background Thread");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        postID =  intent.getIntExtra("post_id",-1);
        notificationValue =  intent.getLongExtra("value",-1);
        method = (NotificationDialog.NotificationMethod) intent.getSerializableExtra("method");
        sessionDetails = (SessionDetails) intent.getSerializableExtra("SessionDetails");
        if (method == NotificationDialog.NotificationMethod.TIME)
            handleTimeNotification();
        else
            handleVoteNotification();
    }

    private void handleVoteNotification() {
        Log.i("NotifierThread", "Creating vote notification");
        currentVoteCount = 0;
        int sleepInterval = 10000;
        while (currentVoteCount < notificationValue) {
            Log.i("NotifierThread", "Checking vote count for post ID: " + postID);
            ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
            Runnable doAtFinish = new Runnable() {
                @Override
                public void run() {
                    currentVoteCount = Integer.parseInt(sessionDetails.responseString);
                    Log.i("NotifierThread", "Vote count received: " + currentVoteCount);
                    if (currentVoteCount >= notificationValue) {
                        Log.i("NotifierThread", "Threshold reached: " + currentVoteCount + "/" + notificationValue);
                        getPost();
                        return;
                    }
                    Log.i("NotifierThread", "Threshold not reached: " + currentVoteCount + "/" + notificationValue);
                }
            };
            connectionManager.getTotalVotes(String.valueOf(postID), doAtFinish);
            try {
                Log.i("NotifierThread", "Waiting " + sleepInterval/1000 + " seconds before trying again");
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                NotificationFileSystem.deleteNotification(postID, getApplicationContext());
                return;
            }
            notificationValue = NotificationFileSystem.getValue(postID,method,getApplicationContext());
        }

    }

    private void handleTimeNotification() {
        Log.i("NotifierThread", "Will notify in " + (notificationValue - System.currentTimeMillis()) + " milliseconds.");
        while (notificationValue > System.currentTimeMillis()) {
            try {
                Thread.sleep(30000);
                notificationValue = NotificationFileSystem.getValue(postID, method, getApplicationContext());
            } catch (InterruptedException e) {
                NotificationFileSystem.deleteNotification(postID, getApplicationContext());
            }
        }
        getPost();
    }


    private void getPost() {
        Log.i("NotifierThread", "Sending Notification");
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        Runnable doAtFinish = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jArray = new JSONArray(sessionDetails.responseString);
                    post = json2post(jArray);
                    SessionDetails.getInstance().responseString = "";
                    SessionDetails.getInstance().post = post;
                    sendNotification();
                    NotificationFileSystem.deleteNotification(postID, getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        connectionManager.getMyPost(String.valueOf(postID), doAtFinish);
    }

    private void sendNotification() {
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());
        if (method == NotificationDialog.NotificationMethod.TIME)
            notification.setContentTitle("Time to follow up on your post!");
        else
            notification.setContentTitle("Your post has reached the requested quota!");

        notification.setContentText("Titled: " + post.title);

        attachIntent();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }

    private void attachIntent() {
        SessionDetails.getInstance().post = post;
        Intent i = new Intent("net.cloudapp.chooser.chooser.Statistics");
        i.putExtra("SessionDetails", sessionDetails);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);
    }

    private Post json2post(JSONArray jArray) throws JSONException {
        JSONObject jObject = jArray.getJSONObject(0);
        String title = jObject.getString("title");
        String image1 = jObject.getString("image1");
        String description1 = jObject.getString("description1");
        String image2 = jObject.getString("image2");
        String description2 = jObject.getString("description2");
        String id = jObject.getString("id");
        int votes1 = Integer.parseInt(jObject.getString("votes1"));
        int votes2 = Integer.parseInt(jObject.getString("votes2"));

        Post newPost = new Post(title, Post.addStroke(Post.string2Bitmap(image1)), description1, Post.addStroke(Post.string2Bitmap(image2)), description2, id, votes1, votes2);
        newPost.setDate(jObject.getString("date"));
        String expDate = jObject.getString("promotion_expiration");
        if (!expDate.equals("null"))
            newPost.setPromotionExpiration(expDate);
        return newPost;
    }

}

