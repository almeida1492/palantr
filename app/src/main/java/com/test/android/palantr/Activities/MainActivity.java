package com.test.android.palantr.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.IdlingResource;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.android.palantr.Adapters.PostsAdapter;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.IdlingResource.SimpleIdlingResource;
import com.test.android.palantr.R;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PostsAdapter adapter;
    RecyclerView postsView;
    ProgressBar progressBar;
    Activity activity = this;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            showLoading();
            ArrayList<Post> postsFromDb = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                Post post = postSnapshot.getValue(Post.class);
                postsFromDb.add(0, post);
            }
            if (postsFromDb.size() != 0){
                ArrayList<Post> sortedPosts = sortPosts(postsFromDb);
                onDone(sortedPosts);
            } else {
                onDone(postsFromDb);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Nullable
    SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();

        /* Necessary for testing
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
        */


        ArrayList<Post> posts = new ArrayList<>();
        adapter = new PostsAdapter(this, posts);
        postsView = findViewById(R.id.post_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        postsView.setLayoutManager(layoutManager);
        postsView.setAdapter(adapter);

        progressBar = findViewById(R.id.progress_bar);

        FloatingActionButton addPostButton = findViewById(R.id.fab);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null),
                        PorterDuff.Mode.MULTIPLY);
        postsView.setVisibility(View.INVISIBLE);
    }

    private void showContent() {
        progressBar.setVisibility(View.INVISIBLE);
        postsView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReference.addValueEventListener(valueEventListener);
    }

    public void onDone(ArrayList<Post> posts) {
        showContent();
        adapter.addPosts(posts);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    public ArrayList<Post> sortPosts(ArrayList<Post> postsFromDb) {
        ArrayList<Post> sortedPosts = new ArrayList<>();
        Post currentPost;
        boolean isAdded;

        currentPost = postsFromDb.get(0);
        sortedPosts.add(currentPost);

        for (int i = 1; i < postsFromDb.size(); i++){
            isAdded = false;
            int j = 0;

            currentPost = postsFromDb.get(i);

            //Get information to compare to settled posts in sortedPosts array
            String strCurrentPostDate = currentPost.getDate().substring(0, 10);
            String strCurrentPostTime = currentPost.getDate().substring(10);
            LocalDate currentPostDate = new LocalDate(strCurrentPostDate);
            LocalTime currentPostTime = new LocalTime(strCurrentPostTime);
            long currentPostVotes = currentPost.getVotes();

            while (!isAdded){

                //Get information to compare to currentPost
                Post settledPost = sortedPosts.get(j);
                String strSettledPostDate = settledPost.getDate().substring(0, 10);
                String strSettledPostTime = settledPost.getDate().substring(10);
                LocalDate settledPostDate = new LocalDate(strSettledPostDate);
                LocalTime settledPostTime = new LocalTime(strSettledPostTime);
                long settledPostVotes = settledPost.getVotes();

                if (currentPostDate.isEqual(settledPostDate)){

                    if (currentPostTime.getHourOfDay() == settledPostTime.getHourOfDay()){

                        if (currentPostVotes == settledPostVotes) {

                            if (currentPostTime.getMinuteOfHour() <= settledPostTime.getMinuteOfHour()){

                                j++;
                            } else {
                                sortedPosts.add(i, currentPost);
                                isAdded = true;
                            }
                        } else if (currentPostVotes <= settledPostVotes){
                            j++;
                        } else {
                            sortedPosts.add(i, currentPost);
                            isAdded = true;
                        }
                    } else if (currentPostTime.getHourOfDay() <= settledPostTime.getHourOfDay()){
                        j++;
                    } else {
                        sortedPosts.add(i, currentPost);
                        isAdded = true;
                    }
                } else if (currentPostDate.isBefore(settledPostDate)){
                    j++;
                } else {
                    sortedPosts.add(i, currentPost);
                    isAdded = true;
                }

                if (!isAdded && j == sortedPosts.size()){
                    sortedPosts.add(currentPost);
                    isAdded = true;
                }
            }
        }

        return sortedPosts;
    }
}
