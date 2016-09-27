package net.cloudapp.chooser.chooser.HttpConnection;

import net.cloudapp.chooser.chooser.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServerAPI {
    @POST("/login")
    Call<Void> login(@Header("token") String token, @Header("userId") String userId);

    @GET("/allPosts")
    Call<List<PostObject>> allPosts();
}
