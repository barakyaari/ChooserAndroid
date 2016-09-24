package net.cloudapp.chooser.chooser;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseConnectionData {
    private String type;
    private String method;
    private String serverAddress;
    private List<Pair<String, String>> pairs;

    public BaseConnectionData(String type, String method, String serverAddress){
        this.pairs = new ArrayList<>();
        this.type = type;
        this.method = method;
        this.serverAddress = serverAddress;
    }

    public void addParameter(String name, String value){
        pairs.add(new Pair<String, String>(name, value));
    }

    public String getAddress(){
        return serverAddress + "/" + method;
    }

    public String getType(){
        return type;
    }

    public List<Pair<String, String>> getHeaders(){
        return this.pairs;
    }

    public byte[] getRequestData(){
        JSONObject json = JsonCreator(pairs);
        return json.toString().getBytes();
    }

    public static JSONObject JsonCreator(List<Pair<String, String>> pairs){
        JSONObject json = new JSONObject();
        try{
            for (Pair<String, String> pair : pairs){
                json.put(pair.first, pair.second);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }
}
