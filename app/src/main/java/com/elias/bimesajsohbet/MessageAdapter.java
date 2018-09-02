package com.elias.bimesajsohbet;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public MessageAdapter (List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_layout_of_users,parent,false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        currentUser = mAuth.getCurrentUser();
        String message_sender_id = mAuth.getCurrentUser().getUid();

        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        if(currentUser != null && message_sender_id != null && fromUserId != null) {

            if (fromUserId.equals(message_sender_id)) {
                holder.messageText.setBackgroundResource(R.drawable.message_text_background_two);

                holder.messageText.setGravity(Gravity.RIGHT);
            } else {
                holder.messageText.setBackgroundResource(R.drawable.message_text_background);

                holder.messageText.setGravity(Gravity.LEFT);
            }
            holder.messageText.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;

        public MessageViewHolder(View view){
            super(view);

            messageText = view.findViewById(R.id.message_text);
        }
    }
}
