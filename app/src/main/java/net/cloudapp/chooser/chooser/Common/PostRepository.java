package net.cloudapp.chooser.chooser.Common;

import net.cloudapp.chooser.chooser.model.Post;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PostRepository {
    public static ConcurrentLinkedQueue<Post> postsFeed = new ConcurrentLinkedQueue<>();
    public static ArrayList<Post> myPosts = new ArrayList<>();
}
