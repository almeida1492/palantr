package com.test.android.palantr.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.test.android.palantr.Adapters.PostListAdapter;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo - 12:00 · 31/02/18", "", 10, 10));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet", null, "Anônimo - 12:00 · 31/02/18", "", 5, 10));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo - 12:00 · 31/02/18", "", -1, 10));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo - 12:00 · 31/02/18", "", 0, 10));

        PostListAdapter adapter = new PostListAdapter(this, posts);
        ListView postsView = findViewById(R.id.post_list);
        postsView.setAdapter(adapter);
    }
}
