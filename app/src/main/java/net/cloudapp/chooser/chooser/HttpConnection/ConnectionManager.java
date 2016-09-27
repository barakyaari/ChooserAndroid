package net.cloudapp.chooser.chooser.HttpConnection;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Barak on 27/09/2016.
 */

public class ConnectionManager {
    private final String chooserServerAddress = "http://192.168.14.37:3000";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(chooserServerAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    PostObject post = retrofit.create(PostObject.class);
}
