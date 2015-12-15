package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import android.telecom.Call;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Barak on 10/12/2015.
 */
public class ConnectionManager {
    private SessionDetails sessionDetails;
    //private final String chooserServerAddress = "http://chooser.cloudapp.net";
    private final String chooserServerAddress = "http://192.168.14.93:8080"; // DONT FORGET TO TURN WIFI ON!!!

    public ConnectionManager(){
        this.sessionDetails = new SessionDetails();
    }

    public ConnectionManager(SessionDetails sessionDetails){
        this.sessionDetails = sessionDetails;
    }

    public void Login(String username, String password, Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("POST", "Login", chooserServerAddress);
        data.addParameter("username", username);
        data.addParameter("password", password);
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        String result = "";
        task.execute(data);
    }

    public void GetPosts(Runnable doAtFinish){
        BaseConnectionData data = new BaseConnectionData("GET", "getAllPosts", chooserServerAddress);
        data.addParameter("id", String.valueOf(sessionDetails.userId));
        ConnectionTask task = new ConnectionTask(doAtFinish, this);
        task.execute(data);
    }

    public void setId(int id){
        this.sessionDetails.userId = id;
    }

    public void setResponseText(String responseText){
        this.sessionDetails.responseString = responseText;
    }

    public void AddPostWithBlob(String title, String image1, String description1, String image2, String description2){
        BaseConnectionData data = new BaseConnectionData("POST", "AddPostWithBlob", chooserServerAddress);
        data.addParameter("title", title);
        data.addParameter("image1", image1);
        data.addParameter("description1", description1);
        data.addParameter("image2", image2);
        data.addParameter("description2", description2);
        ConnectionTask task = new ConnectionTask(null, this);
        task.execute(data);
    }

    public SessionDetails getSessionDetails(){
        return this.sessionDetails;
    }

}
