package net.cloudapp.chooser.chooser.Common;

import android.content.Context;

import net.cloudapp.chooser.chooser.views.dialogs.LoadingDialog;

import java.util.HashMap;

/**
 * Created by Ben on 22/10/2016.
 */
public class LoadingDialogs {
    private static HashMap<String,LoadingDialog> map = new HashMap<>();

    public static void show(String key) {
        map.get(key).show();
    }

    public static void hide(String key) {
        map.get(key).hide();
    }

    public static void addLoadingDialog(Context context, String key, String message) {
        if (!map.containsKey("key"))
            map.put(key,new LoadingDialog(context, message));
    }

    public static void deleteLoadingDialog (String key) {
        LoadingDialog ld = map.remove(key);
        if (ld.isShowing())
            ld.hide();
    }

}
