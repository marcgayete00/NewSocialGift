package com.example.newsocialgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.ChatModel;
import com.example.newsocialgift.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_OTHER = 0;
    private static final int VIEW_TYPE_CURRENT_USER = 1;

    private List<ChatModel> mData;
    private String mCurrentUserId;

    public ChatAdapter(List<ChatModel> data, String currentUserId) {
        mData = data;
        mCurrentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel message = mData.get(position);
        if (message.getSender().equals(mCurrentUserId)) {
            return VIEW_TYPE_CURRENT_USER;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CURRENT_USER) {
            View view = inflater.inflate(R.layout.item_chat_right, parent, false);
            return new CurrentUserViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_chat_left, parent, false);
            return new OtherUserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatModel message = mData.get(position);

        if (holder instanceof CurrentUserViewHolder) {
            CurrentUserViewHolder currentUserViewHolder = (CurrentUserViewHolder) holder;
            currentUserViewHolder.bind(message);
        } else if (holder instanceof OtherUserViewHolder) {
            OtherUserViewHolder otherUserViewHolder = (OtherUserViewHolder) holder;
            otherUserViewHolder.bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ChatModel getItem(int position) {
        return mData.get(position);
    }

    public static class CurrentUserViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessageTextView;

        public CurrentUserViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.rightMessageTextView);
        }

        public void bind(ChatModel message) {
            mMessageTextView.setText(message.getMessage());
        }
    }

    public static class OtherUserViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessageTextView;

        public OtherUserViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.leftMessageTextView);
        }

        public void bind(ChatModel message) {
            mMessageTextView.setText(message.getMessage());
        }
    }
}

