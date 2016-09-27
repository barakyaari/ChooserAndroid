package net.cloudapp.chooser.chooser.HttpConnection;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PostObject {

    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("url1")
    @Expose
    private Object url1;
    @SerializedName("description1")
    @Expose
    private Object description1;
    @SerializedName("url2")
    @Expose
    private Object url2;
    @SerializedName("description2")
    @Expose
    private Object description2;
    @SerializedName("userId")
    @Expose
    private Object userId;
    @SerializedName("postId")
    @Expose
    private String postId;

    /**
     * @return The title
     */
    public Object getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(Object title) {
        this.title = title;
    }

    /**
     * @return The url1
     */
    public Object getUrl1() {
        return url1;
    }

    /**
     * @param url1 The url1
     */
    public void setUrl1(Object url1) {
        this.url1 = url1;
    }

    /**
     * @return The description1
     */
    public Object getDescription1() {
        return description1;
    }

    /**
     * @param description1 The description1
     */
    public void setDescription1(Object description1) {
        this.description1 = description1;
    }

    /**
     * @return The url2
     */
    public Object getUrl2() {
        return url2;
    }

    /**
     * @param url2 The url2
     */
    public void setUrl2(Object url2) {
        this.url2 = url2;
    }

    /**
     * @return The description2
     */
    public Object getDescription2() {
        return description2;
    }

    /**
     * @param description2 The description2
     */
    public void setDescription2(Object description2) {
        this.description2 = description2;
    }

    /**
     * @return The userId
     */
    public Object getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */

    public void setUserId(Object userId) {
        this.userId = userId;
    }
    /**
     * @return The userId
     */
    public String getPostId() {
        return postId;
    }

    /**
     * @param userId The userId
     */
    public void setPostId(String userId) {
        this.userId = userId;
    }

}
