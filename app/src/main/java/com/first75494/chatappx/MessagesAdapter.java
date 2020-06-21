package com.first75494.chatappx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Messages> userMessageList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public MessagesAdapter (List<Messages> userMessageList){
        this.userMessageList = userMessageList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);

        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.custom_message_layout,viewGroup,false);

       firebaseFirestore = FirebaseFirestore.getInstance();
       firebaseAuth = FirebaseAuth.getInstance();

       return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {

         String messageSenderId = firebaseAuth.getCurrentUser().getUid();
         Messages messages = userMessageList.get(i);

         String fromUserId = messages.getFrom();
         String fromMessageType = messages.getType();

         if(fromMessageType.equals("text")){
             messageViewHolder.receiverMessageText.setVisibility(View.INVISIBLE);
             messageViewHolder.receiverProfileImage.setVisibility(View.INVISIBLE); //profile pic part todo

             if(fromUserId.equals(messageSenderId)){
                 messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_message_layout);
                 messageViewHolder.senderMessageText.setText(messages.getMessage());
             }else{
                 messageViewHolder.senderMessageText.setVisibility(View.INVISIBLE);

                 messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE); //todo
                 messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);

                 messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.reciever_message_layout);
                 messageViewHolder.receiverMessageText.setText(messages.getMessage());
             }
         }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}
