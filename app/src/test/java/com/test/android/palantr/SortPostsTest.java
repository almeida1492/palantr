package com.test.android.palantr;

import com.test.android.palantr.Activities.MainActivity;
import com.test.android.palantr.Entities.Post;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by henriquedealmeida on 28/05/18.
 */
public class SortPostsTest {
    @Test
    public void sortPosts() throws Exception {
        ArrayList<Post> rawPosts = new ArrayList<>();

        rawPosts.add(new Post(1, 1, "teste", null, "henrique", "geral", (long)10, "2018-05-23T10:10:39.355"));
        rawPosts.add(new Post(1, 1, "teste", null, "henrique", "geral", (long)5, "2018-05-15T10:12:39.355"));
        rawPosts.add(new Post(1, 1, "teste", null, "henrique", "geral", (long)0, "2018-05-23T10:14:39.355"));
        rawPosts.add(new Post(1, 1, "teste", null, "henrique", "geral", (long)20, "2018-05-31T10:18:39.355"));
        rawPosts.add(new Post(1, 1, "teste", null, "henrique", "geral", (long)-4, "2018-05-01T10:09:39.355"));

        ArrayList<Post> sortedPosts = new MainActivity().sortPosts(rawPosts);

        /*assertThat().isEqualToComparingFieldByField(rawPosts);*/
    }

}