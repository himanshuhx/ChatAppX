package com.first75494.chatappx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.MediaDrm;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverId, messageReceiverName,messageSenderId;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private Toolbar chatToolBar;

    private ImageButton sendMessageBtn;
    private EditText messageInputText;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messagesAdapter;
    private RecyclerView  userMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        messageSenderId = firebaseAuth.getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();

        messageReceiverId = Objects.requireNonNull(getIntent().getExtras().get("visit_user_id")).toString();
        messageReceiverName = Objects.requireNonNull(getIntent().getExtras().get("visit_user_name")).toString();

        Toast.makeText(ChatActivity.this,messageReceiverId,Toast.LENGTH_SHORT).show();
        Toast.makeText(ChatActivity.this,messageReceiverName,Toast.LENGTH_SHORT).show();

        InitializeControllers();

        userName.setText(messageReceiverName);
        //image part

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
    }


    private void InitializeControllers() {

        chatToolBar = findViewById(R.id.chat_toolbar);

        setSupportActionBar(chatToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);

        userImage = findViewById(R.id.custom_profile_image);
        userName = findViewById(R.id.custom_profile_name);
        userLastSeen= findViewById(R.id.custom_last_seen);

        sendMessageBtn = findViewById(R.id.send_message_btn);
        messageInputText = findViewById(R.id.input_message);

        messagesAdapter = new MessagesAdapter(messagesList);
        userMessagesList = findViewById(R.id.private_message_list);
        userMessagesList.setLayoutManager(new LinearLayoutManager(this));
        //linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setAdapter(messagesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String messageId =  firestore.collection("messages").document(messageSenderId)
                .collection(messageReceiverId).document().getId();


        firestore.collection("messages").document(messageSenderId)
                .collection(messageReceiverId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            messagesList.add(queryDocumentSnapshots.getDocuments().get(0).toObject(Messages.class));

                                messagesAdapter.notifyDataSetChanged();

                }});

    }

    private void SendMessage() {
        final String messageText = messageInputText.getText().toString();

        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(this,"Please Enter Message...",Toast.LENGTH_SHORT).show();
        }else{
           final String messageId =  firestore.collection("messages").document(messageSenderId)
                     .collection(messageReceiverId).document().getId();

            Map<String,Object> messageBody = new HashMap<>();
            messageBody.put("message",messageText);
            messageBody.put("type","text");
            messageBody.put("from",messageSenderId);


            firestore.collection("messages").document(messageSenderId)
                    .collection(messageReceiverId).document(messageId)
                    .set(messageBody)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Map<String,Object> messageBody = new HashMap<>();
                                messageBody.put("message",messageText);
                                messageBody.put("type","text");
                                messageBody.put("from",messageSenderId);


                                firestore.collection("messages").document(messageReceiverId)
                                        .collection(messageSenderId).document(messageId)
                                        .set(messageBody)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ChatActivity.this,"Message Sent",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
        }
    }

}
