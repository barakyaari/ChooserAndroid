package net.cloudapp.chooser.chooser;


import android.content.pm.PackageInstaller;
import android.util.Log;

import com.facebook.AccessToken;

public class ConnectionManager {
    private SessionDetails sessionDetails;


    public ConnectionManager(){
        this.sessionDetails = new SessionDetails();
    }

    public ConnectionManager(SessionDetails sessionDetails){
        this.sessionDetails = sessionDetails;
    }


    public void login(Runnable doAtFinish){
        SessionDetails sessionDetails = SessionDetails.getInstance();
        AccessToken token = sessionDetails.getAccessToken();
        Log.d("Chooser", "Calling Login at: " + sessionDetails.serverAddress);
        BaseConnectionData data = new BaseConnectionData("POST", "login", sessionDetails.serverAddress);
        data.addParameter("token", token.getToken());
        data.addParameter("userId", token.getUserId());
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void updateUser(String id, String firstName, String lastName, String email, String gender, String birthDate){
        SessionDetails sessionDetails = SessionDetails.getInstance();

        BaseConnectionData data = new BaseConnectionData("POST", "updateUser", sessionDetails.serverAddress);
        String sqlBdate = birthDate.substring(6) + "-" + birthDate.substring(0,2) + "-" + birthDate.substring(3,5) + " 12:00:00.000";
        System.out.println(sqlBdate);
        data.addParameter("ID", id);
        data.addParameter("FirstName", firstName);
        data.addParameter("LastName", lastName);
        data.addParameter("Email", email);
        data.addParameter("Gender", gender);
        data.addParameter("BirthDate", sqlBdate);

        ConnectionTask task = new ConnectionTask(this);
        task.execute(data);
    }

    public void GetPosts(Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getAllPosts", sessionDetails.serverAddress);
        data.addParameter("access_token", SessionDetails.getInstance().getAccessToken().getToken());
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void getMyPosts(Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getMyPosts", sessionDetails.serverAddress);
        data.addParameter("uID", String.valueOf(sessionDetails.userId));
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void getMyPost(String post_id, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getMyPost", sessionDetails.serverAddress);
        data.addParameter("uID", String.valueOf(sessionDetails.userId));
        data.addParameter("post_id", post_id);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void getTotalVotes(String post_id, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getTotalVotes", sessionDetails.serverAddress);
        data.addParameter("uID", String.valueOf(sessionDetails.userId));
        data.addParameter("post_id", post_id);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void getStatistics(String post_id, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getStatistics", sessionDetails.serverAddress);
        data.addParameter("post_id", post_id);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void addPromotion(String user_id, String post_id, int promotionDuration, String promotionTime, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("POST", "addPromotion", sessionDetails.serverAddress);
        data.addParameter("user_id", user_id);
        data.addParameter("post_id", post_id);
        data.addParameter("promotionDuration", String.valueOf(promotionDuration));
        data.addParameter("promotionTime", promotionTime);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void setId(String id){
        this.sessionDetails.userId = id;
    }

    public void setResponseText(String responseText){
        this.sessionDetails.responseString = responseText;
    }

    public void AddPostWithBlob(String title, String image1, String description1, String image2, String description2, Runnable doAtFinish, int promotionDuration, String promotionTime){
        BaseConnectionData data = new BaseConnectionData("POST", "AddPostWithBlob", sessionDetails.serverAddress);
        data.addParameter("title", title);
        data.addParameter("image1", image1);
        data.addParameter("description1", description1);
        data.addParameter("image2", image2);
        data.addParameter("description2", description2);
        data.addParameter("user_id", String.valueOf(sessionDetails.userId));
        data.addParameter("promotionDuration", String.valueOf(promotionDuration));
        data.addParameter("promotionTime", promotionTime);

        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void vote(String vote, String post_id, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("POST", "vote", sessionDetails.serverAddress);
        data.addParameter("vote", vote);
        data.addParameter("uid", sessionDetails.userId);
        data.addParameter("id", post_id);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void getTokenCount(String uID, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getTokenCount", sessionDetails.serverAddress);
        data.addParameter("uid", sessionDetails.userId);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void reportPost(String post_id, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("POST", "report", sessionDetails.serverAddress);
        data.addParameter("id", post_id);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void deletePost(String id, Runnable doAtFinish) {
        BaseConnectionData data = new BaseConnectionData("POST", "deletePost", sessionDetails.serverAddress);
        data.addParameter("id", String.valueOf(id));
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }
}
