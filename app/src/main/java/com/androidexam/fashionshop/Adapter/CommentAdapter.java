package com.androidexam.fashionshop.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Decoration.DateTimeHelper;
import com.androidexam.fashionshop.Model.Comment;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);


        holder.commentUsername.setText(comment.getUsername());
        holder.commentContent.setText(comment.getContent());


        String formattedDateTime = DateTimeHelper.formatDateTime(comment.getTime());
        holder.commentTime.setText(formattedDateTime);

        holder.rate.setRating(comment.getRate());
        if (comment.getUrl() == null) {
            String name = comment.getUsername();
            String firstLetter = name.substring(0, 1).toLowerCase();

            int imageResource = context.getResources().getIdentifier(
                    firstLetter,
                    "drawable",
                    context.getPackageName()

            );
        Log.d("AvatarImage", "Image Resource: " + imageResource);
            holder.avatar.setImageResource(imageResource);
        }
        else {
            Picasso.get().load(comment.getUrl()).into(holder.avatar);
        }





    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentUsername;
        public TextView commentContent;
        public TextView commentTime;
        public ImageView avatar;
        public RatingBar rate;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUsername = itemView.findViewById(R.id.commentUsername);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentTime = itemView.findViewById(R.id.commentTime);
            avatar = itemView.findViewById(R.id.avatarImageView);
            rate = itemView.findViewById(R.id.ratingBarcm);
        }
    }

}

