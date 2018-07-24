package com.test.android.palantr.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private String LOG_TAG = PostsAdapter.class.getName();

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
    public void onBindViewHolder(@NonNull final PostsAdapter.ViewHolder holder, final int position) {
        final Post currentPost = mPosts.get(position);

        holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));
        GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
        int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
        voteCounterShape.setColor(voteCounterColor);

        holder.bodyView.setText(currentPost.getBody());

        //TODO it has to get bitmap from currentPost instead
        Bitmap rawPicture = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder);

        if (rawPicture != null){
            Bitmap roundedCornerPicture = getRoundedCornerBitmap(rawPicture);
            holder.pictureView.setVisibility(View.VISIBLE);
            holder.pictureView.setImageBitmap(roundedCornerPicture);
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

        holder.voteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPost.setVotes(currentPost.getVotes() + 1);
                holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));

                GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
                int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
                voteCounterShape.setColor(voteCounterColor);
            }
        });

        holder.voteDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPost.setVotes(currentPost.getVotes() - 1);
                holder.voteCounterView.setText(String.valueOf(currentPost.getVotes()));

                GradientDrawable voteCounterShape = (GradientDrawable) holder.voteCounterView.getBackground();
                int voteCounterColor = getVoteCounterColor(currentPost.getVotes());
                voteCounterShape.setColor(voteCounterColor);
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

        public ViewHolder(View itemView) {
            super(itemView);
            voteCounterView = itemView.findViewById(R.id.vote_counter);
            bodyView = itemView.findViewById(R.id.post_body);
            pictureView = itemView.findViewById(R.id.post_picture);
            signatureView = itemView.findViewById(R.id.post_signature);
            dateView = itemView.findViewById(R.id.post_date);
            voteUpButton = itemView.findViewById(R.id.vote_up);
            voteDownButton = itemView.findViewById(R.id.vote_down);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
