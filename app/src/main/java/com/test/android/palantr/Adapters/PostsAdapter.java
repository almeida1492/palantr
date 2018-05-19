package com.test.android.palantr.Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Post> mPosts;

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
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post currentPost = mPosts.get(position);
        holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));
        GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
        int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
        voteCounterShape.setColor(voteCounterColor);
        holder.bodyView.setText(currentPost.getBody());
        holder.signatureView.setText(currentPost.getSignature());

        String date = currentPost.getDate();
        /*
        LocalDate date = LocalDate.parse(rawDate.substring(0, 10));
        LocalTime time = LocalTime.parse(rawDate.substring(11, 18));

        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthOfYear());
        String year = String.format("%d", date.getYear());
        String hour = String.format("%02d", time.getHourOfDay());
        String minute = String.format("%02d", time.getMinuteOfHour());

        String output = " - " + hour + ":" + minute + " · " + day + "/" + month + "/" + year;
        */

        holder.dateView.setText(date);
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
        mPosts.addAll(posts);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView voteCounterView;
        public final TextView bodyView;
        public final TextView signatureView;
        public final TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            voteCounterView = itemView.findViewById(R.id.vote_counter);
            bodyView = itemView.findViewById(R.id.post_body);
            signatureView = itemView.findViewById(R.id.post_signature);
            dateView = itemView.findViewById(R.id.post_date);
        }
    }
}
