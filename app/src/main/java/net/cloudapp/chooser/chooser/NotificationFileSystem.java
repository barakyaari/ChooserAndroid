package net.cloudapp.chooser.chooser;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

/**
 * Created by Ben on 11/09/2016.
 */
public class NotificationFileSystem {

    public static void addNotification(int postID, long notificationVal, NotificationDialog.NotificationMethod method, SessionDetails sessionDetails, Context context) {
        Log.i("NotificationSystem", "Received new notification. ID: " + postID + " Method: " + method.toString() + " Value: " + notificationVal);
        Hashtable<Integer,Long> table = getTable(method,context);
        if (table == null)
            table = new Hashtable<>();


        if (exists(postID, method, context)) {
            table.put(postID, notificationVal);
            setTable(table, method, context);
        } else {
            table.put(postID, notificationVal);
            setTable(table, method, context);

            Intent intent = new Intent(context, NotifierBackgroundThread.class);
            intent.putExtra("value", notificationVal);
            intent.putExtra("method", method);
            intent.putExtra("post_id", postID);
            intent.putExtra("SessionDetails", sessionDetails);
            context.startService(intent);

            Log.i("NotificationSystem", "Notification added to table: " + table);
        }
    }

    public static long getValue(int postID, NotificationDialog.NotificationMethod method, Context context) {
        Hashtable<Integer,Long> table = getTable(method, context);
        if (table != null && table.containsKey(postID)) {
            long val = table.get(postID);
            Log.i("NotificationSystem", "Retrieved value: " + val + " for postID: " + postID);
            return val;
        }
        return -1;
    }

    public static boolean exists (int postID, NotificationDialog.NotificationMethod method, Context context) {
        Hashtable<Integer,Long> table = getTable(method, context);
        return table.containsKey(postID);
    }

    public static void deleteNotification(int postID, Context context) {
        Hashtable<Integer,Long> table = getTable(NotificationDialog.NotificationMethod.TIME,context);
        if (table != null && table.containsKey(postID)) {
            table.remove(postID);
            setTable(table, NotificationDialog.NotificationMethod.TIME, context);
            Log.i("NotificationSystem", "Notification removed from Time Table: " + table);
            return;
        }

        table = getTable(NotificationDialog.NotificationMethod.VOTES,context);
        if (table != null && table.containsKey(postID)) {
            table.remove(postID);
            setTable(table, NotificationDialog.NotificationMethod.VOTES, context);
            Log.i("NotificationSystem", "Notification Removed from Votes Table: " + table);
        }
    }

    public static void clearTables(Context context) {
        for (NotificationDialog.NotificationMethod method : NotificationDialog.NotificationMethod.values()) {
            Hashtable<Integer, Long> table = getTable(method, context);
            if (table != null) {
                table.clear();
                setTable(table, method, context);
            }
            Log.i("NotificationSystem", "Table " + method.toString() + " Cleared!");
        }
    }

    public static boolean isEmpty(NotificationDialog.NotificationMethod method, Context context) {
        Hashtable<Integer, Long> table = getTable(method, context);
        return table == null || table.isEmpty();
    }

    private static Hashtable<Integer,Long> getTable(NotificationDialog.NotificationMethod method, Context context) {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        Hashtable<Integer,Long> notificationTable = new Hashtable<>();

        try {
            try {
                fileInputStream = context.openFileInput("NotificationTable." + method.toString());
                objectInputStream = new ObjectInputStream(fileInputStream);

                try {
                    notificationTable = (Hashtable) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {}

                objectInputStream.close();

            } catch (FileNotFoundException e) {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return notificationTable;
    }

    private static synchronized void setTable (Hashtable<Integer,Long> table, NotificationDialog.NotificationMethod method, Context context) {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        try {
            fileOutputStream = context.openFileOutput("NotificationTable." + method.toString(), Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(table);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
