package com.test.android.palantr.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.android.palantr.Adapters.PostListAdapter;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Post> postsFromDb = new ArrayList<>();
    PostListAdapter adapter;
    ListView postsView;
    Activity activity = this;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                int id = Integer.parseInt(postSnapshot.getKey());
                String body = null;
                String creator = null;
                String topic = null;
                Long votes = 0L;
                for (DataSnapshot attributeSnapshot : postSnapshot.getChildren()) {
                    switch (attributeSnapshot.getKey()) {
                        case "body":
                            body = String.valueOf(attributeSnapshot.getValue());
                            break;
                        case "creator":
                            creator = String.valueOf(attributeSnapshot.getValue());
                            break;
                        case "topic":
                            topic = String.valueOf(attributeSnapshot.getValue());
                            break;
                        case "votes":
                            votes = (Long) attributeSnapshot.getValue();
                            break;
                    }
                }
                //TODO missing date
                Post post = new Post(id, id, body, null, creator, topic, votes, "2009-06-01T13:45:30");
                postsFromDb.add(post);
            }
            adapter = new PostListAdapter(activity, postsFromDb);
            postsView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 10L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet", null, "Anônimo", "", 5L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", -1L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        posts.add(new Post(1, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pel", null, "Anônimo", "", 0L, "2009-06-01T13:45:30"));
        adapter = new PostListAdapter(this, posts);
        postsView = findViewById(R.id.post_list);
        postsView.setAdapter(adapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReference.addValueEventListener(valueEventListener);

    }
}
