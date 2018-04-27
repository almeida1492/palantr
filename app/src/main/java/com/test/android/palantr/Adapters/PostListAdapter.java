package com.test.android.palantr.Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import java.util.ArrayList;

/**
 * Created by henriquedealmeida on 26/04/18.
 */

public class PostListAdapter extends ArrayAdapter<Post> {

    public PostListAdapter(@NonNull Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Post currentPost = getItem(position);

        TextView voteCounterView = listItemView.findViewById(R.id.vote_counter);
        voteCounterView.setText(String.valueOf(currentPost.getVotes()));

        GradientDrawable voteCounterShape = (GradientDrawable) voteCounterView.getBackground();
        int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
        voteCounterShape.setColor(voteCounterColor);

        TextView bodyView = listItemView.findViewById(R.id.post_body);
        bodyView.setText(currentPost.getBody());

        TextView signatureView = listItemView.findViewById(R.id.post_signature);
        signatureView.setText(currentPost.getSignature());

        return listItemView;
    }

    private int getVoteCounterColor(int votes){
        if (votes >= 10){
            return ContextCompat.getColor(getContext(), R.color.colorSuccess);
        } else if (votes < 0){
            return ContextCompat.getColor(getContext(), R.color.colorDanger);
        }
        return ContextCompat.getColor(getContext(), R.color.colorWarning);
    }
}
