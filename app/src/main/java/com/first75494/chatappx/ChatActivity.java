package com.first75494.chatappx;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverId, messageReceiverName;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private Toolbar chatToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageReceiverId = Objects.requireNonNull(getIntent().getExtras().get("visit_user_id")).toString();
        messageReceiverName = Objects.requireNonNull(getIntent().getExtras().get("visit_user_name")).toString();

        Toast.makeText(ChatActivity.this,messageReceiverId,Toast.LENGTH_SHORT).show();
        Toast.makeText(ChatActivity.this,messageReceiverName,Toast.LENGTH_SHORT).show();

        InitializeControllers();

        userName.setText(messageReceiverName);
        //image part
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
    }
}
