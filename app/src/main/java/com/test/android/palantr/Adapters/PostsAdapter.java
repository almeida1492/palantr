package com.test.android.palantr.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.test.android.palantr.Activities.MainActivity;
import com.test.android.palantr.Activities.PictureViewer;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private String LOG_TAG = PostsAdapter.class.getName();

    private Context mContext;
    private ArrayList<Post> mPosts;
    private boolean isImageFitToScreen = false;

    public PostsAdapter(@NonNull Context context, ArrayList<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutIdForListItem = R.layout.list_item;
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsAdapter.ViewHolder holder, final int position) {
        final Post currentPost = mPosts.get(position);

        holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));
        GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
        int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
        voteCounterShape.setColor(voteCounterColor);

        holder.bodyView.setText(currentPost.getBody());

        //Set blinkView color and visibility
        holder.blinkView.setVisibility(View.INVISIBLE);
        GradientDrawable blinkViewShape = (GradientDrawable) holder.blinkView.getBackground();
        blinkViewShape.setColor(ContextCompat.getColor(mContext, R.color.blink));

        if (currentPost.getMedia() != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(currentPost.getId_post());
            Log.i("media", "Has media");
            storageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(final byte[] bytes) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.pictureView.setImageBitmap(bitmap);
                    holder.pictureView.setClipToOutline(true);
                    holder.pictureView.setVisibility(View.VISIBLE);
                    holder.pictureView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, PictureViewer.class);

                            intent.putExtra("postId", currentPost.getId_post());
                            mContext.startActivity(intent);
                        }
                    });
                }
            });
        } else {
            holder.pictureView.setVisibility(View.GONE);
        }

        holder.signatureView.setText(currentPost.getSignature());

        /*String date = currentPost.getDate();*/
        LocalDateTime dateTime = LocalDateTime.parse(currentPost.getDate());
        /*LocalTime time = LocalTime.parse(currentPost.getDate());*/

        String day = String.format("%02d", dateTime.getDayOfMonth());
        String month = String.format("%02d", dateTime.getMonthOfYear());
        String year = String.format("%d", dateTime.getYear());
        String hour = String.format("%02d", dateTime.getHourOfDay());
        final String minute = String.format("%02d", dateTime.getMinuteOfHour());

        String output = " - " + hour + ":" + minute + " · " + day + "/" + month + "/" + year;

        holder.dateView.setText(output);

        //Declare and set animation elements that are going to be used in the vote click events
        final AlphaAnimation anim = new AlphaAnimation(0.5f, 0.0f);
        anim.setDuration(3000);
        anim.setRepeatMode(Animation.REVERSE);

        holder.voteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPost.setVotes(currentPost.getVotes() + 1);
                holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));

                String postKeyInDb = currentPost.getId_post();
                DatabaseReference postInDb = FirebaseDatabase.getInstance().getReference()
                        .child("posts").child(postKeyInDb);
                postInDb.child("votes").setValue(currentPost.getVotes());

                GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
                int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
                voteCounterShape.setColor(voteCounterColor);

                holder.blinkView.startAnimation(anim);
            }
        });

        holder.voteDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPost.setVotes(currentPost.getVotes() - 1);
                holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));

                String postKeyInDb = currentPost.getId_post();
                DatabaseReference postInDb = FirebaseDatabase.getInstance().getReference()
                        .child("posts").child(postKeyInDb);
                postInDb.child("votes").setValue(currentPost.getVotes());

                GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
                int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
                voteCounterShape.setColor(voteCounterColor);

                holder.blinkView.startAnimation(anim);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPosts == null) return 0;
        return mPosts.size();
    }

    private int getVoteCounterColor(Long votes) {
        if (votes >= 10) {
            return ContextCompat.getColor(mContext, R.color.colorSuccess);
        } else if (votes < 0) {
            return ContextCompat.getColor(mContext, R.color.colorDanger);
        }
        return ContextCompat.getColor(mContext, R.color.colorWarning);
    }

    public ArrayList<Post> getPosts() {
        return mPosts;
    }

    public void setPosts(ArrayList<Post> posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    public void addPosts(ArrayList<Post> posts) {
        mPosts.clear();
        mPosts.addAll(posts);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView voteCounterView;
        public final TextView bodyView;
        public final ImageView pictureView;
        public final TextView signatureView;
        public final TextView dateView;
        public final Button voteUpButton;
        public final Button voteDownButton;
        public final View blinkView;

        public ViewHolder(View itemView) {
            super(itemView);
            voteCounterView = itemView.findViewById(R.id.vote_counter);
            bodyView = itemView.findViewById(R.id.post_body);
            pictureView = itemView.findViewById(R.id.post_picture);
            signatureView = itemView.findViewById(R.id.post_signature);
            dateView = itemView.findViewById(R.id.post_date);
            voteUpButton = itemView.findViewById(R.id.vote_up);
            voteDownButton = itemView.findViewById(R.id.vote_down);
            blinkView = itemView.findViewById(R.id.blink);
        }
    }
}
