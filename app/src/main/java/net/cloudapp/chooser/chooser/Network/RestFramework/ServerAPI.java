package net.cloudapp.chooser.chooser.Network.RestFramework;

import net.cloudapp.chooser.chooser.model.Post;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;

public interface ServerAPI {
    @GET("/login")
    public void login(
            @Header("token") String token,
            @Header("userId") String userId,
            Callback<Void> callback);

    @GET("/allPosts")
    public void allPosts(
            @Header("token") String token,
            Callback<List<Post>> callback);

    @GET("/addpost")
    public void addpost(
            @Header("token") String token,
            @Header("title") String title,
            @Header("image1") String image1,
            @Header("image2") String image2,
            @Header("description1") String description1,
            @Header("description2") String description2,
            Callback<Void> callback);
}