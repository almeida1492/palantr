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

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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
        //2009-06-01T13:45:30

        //Vote counter rendering
        TextView voteCounterView = listItemView.findViewById(R.id.vote_counter);
        voteCounterView.setText(String.valueOf(currentPost.getVotes()));

        GradientDrawable voteCounterShape = (GradientDrawable) voteCounterView.getBackground();
        int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
        voteCounterShape.setColor(voteCounterColor);

        //Text body rendering
        TextView bodyView = listItemView.findViewById(R.id.post_body);
        bodyView.setText(currentPost.getBody());

        //Signature rendering
        TextView signatureView = listItemView.findViewById(R.id.post_signature);
        signatureView.setText(currentPost.getSignature());

        //Date and time rendering
        JodaTimeAndroid.init(getContext());
        String rawDate = currentPost.getDate();
        LocalDate date = LocalDate.parse(rawDate.substring(0, 10));
        LocalTime time = LocalTime.parse(rawDate.substring(11, 18));

        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthOfYear());
        String year = String.format("%d", date.getYear());
        String hour = String.format("%02d", time.getHourOfDay());
        String minute = String.format("%02d", time.getMinuteOfHour());

        String output = " - " + hour + ":" + minute + " · " + day + "/" + month + "/" + year;
        TextView dateView = listItemView.findViewById(R.id.post_date);
        dateView.setText(output);

        return listItemView;
    }

    private int getVoteCounterColor(Long votes){
        if (votes >= 10){
            return ContextCompat.getColor(getContext(), R.color.colorSuccess);
        } else if (votes < 0){
            return ContextCompat.getColor(getContext(), R.color.colorDanger);
        }
        return ContextCompat.getColor(getContext(), R.color.colorWarning);
    }
}
