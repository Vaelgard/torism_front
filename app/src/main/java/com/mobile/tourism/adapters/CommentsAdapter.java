package com.mobile.tourism.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.tourism.R;
import com.mobile.tourism.models.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private final List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for comment items
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_comments_list, parent, false); // Correct layout for item
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if (comment != null) {
            Log.d("CommentsAdapter", "Email: " + comment.getEmail());
            Log.d("CommentsAdapter", "Text: " + comment.getText());

            if (comment.getEmail() != null) {
                holder.email.setText(comment.getEmail());
            } else {
                holder.email.setText("No email available");
            }

            if (comment.getText() != null) {
                holder.text.setText(comment.getText());
            } else {
                holder.text.setText("No text available");
            }
        } else {
            Log.d("CommentsAdapter", "Comment is null at position: " + position);
        }
    }


    @Override
    public int getItemCount() {
        return (comments != null) ? comments.size() : 0; // Null check to avoid NPE
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, email;

        ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.comment_text);
            email = view.findViewById(R.id.comment_email);
        }
    }
}
