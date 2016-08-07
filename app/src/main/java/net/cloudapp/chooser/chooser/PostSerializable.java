package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by Ben on 09/07/2016.
 */
public class PostSerializable implements Serializable {
    public String title;
    public String description1;
    public String description2;
    public GregorianCalendar date;
    public String id;
    public String image1Serializable;
    public String image2Serializable;
    public int votes1;
    public int votes2;
    public PostStatistics postStatistics;

    public PostSerializable (Post post) {
        this.title = post.title;
        this.description1 = post.description1;
        this.description2 = post.description2;
        this.date = post.date;
        this.id = post.id;
        this.votes1 = post.votes1;
        this.votes2 = post.votes2;
        this.image1Serializable = Post.bitmap2String(post.image1,80,60);
        this.image2Serializable = Post.bitmap2String(post.image2,80,60);
        this.postStatistics = post.postStatistics;
    }

    public Post getPost() {
        Bitmap image1 = Post.string2Bitmap(image1Serializable);
        Bitmap image2 = Post.string2Bitmap(image2Serializable);
        Post post = new Post(title,image1,description1,image2,description2,id,votes1,votes2);
        post.date = date;
        post.postStatistics = postStatistics;
        return post;
    }


}
