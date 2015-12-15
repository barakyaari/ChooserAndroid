package net.cloudapp.chooser.chooser;

import java.io.Serializable;

/**
 * Created by Barak on 10/12/2015.
 */
public class SessionDetails implements Serializable{
    public int userId;
    public String responseString;
    public SessionDetails(){
        userId = -2;
    }
}
