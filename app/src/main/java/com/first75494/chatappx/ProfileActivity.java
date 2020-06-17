package com.first75494.chatappx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private  String receiverUserId,senderUserId,current_state,fSenderId;

    private TextView userProfileName, userProfileEmail;
    private Button  sendMessageRequestButton;
    private CircleImageView userProfileImage;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        senderUserId = fAuth.getCurrentUser().getUid();
        fSenderId = senderUserId.toString();
        documentReference = fStore.collection("users").document(receiverUserId);

        Toast.makeText(this,"User ID: "+receiverUserId,Toast.LENGTH_SHORT).show();

        userProfileImage =  findViewById(R.id.visit_profile_image);
        userProfileName = findViewById(R.id.visit_profile_name);
        sendMessageRequestButton = findViewById(R.id.send_message_request_button);
        userProfileEmail = findViewById(R.id.visit_profile_email);
        current_state = "new";

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            String username = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");

                            userProfileName.setText(username);
                            userProfileEmail.setText(email);
                            // profile pic part

                            ManageChatRequests();
                        }else {
                            Toast.makeText(ProfileActivity.this,"User doesn't have data",Toast.LENGTH_SHORT).show();
                            ManageChatRequests();
                        }
                    }
                });
    }

    private void ManageChatRequests() {

        DocumentReference docRef = fStore.collection("ChatRequests").document(senderUserId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String request_type = document.getString("request_type").toString();
                        String id = document.getString("sender_id").toString();

                        if(request_type.equals("received") && id.equals(fSenderId)){
                            current_state = "request_sent";
                            sendMessageRequestButton.setText("Cancel Chat Request");
                        }
                    } else {
                       Toast.makeText(ProfileActivity.this,"Error !!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this,"Error !!",Toast.LENGTH_SHORT).show();
                }
            }
        });

     if(!senderUserId.equals(receiverUserId)){
         sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendMessageRequestButton.setEnabled(false);

                 if(current_state.equals("new")){
                     sendChatRequest();
                 }
             }
         });
     }else{
         sendMessageRequestButton.setVisibility(View.INVISIBLE);
     }
    }

    private void sendChatRequest() {

        Map<String,Object> sender = new HashMap<>();
        sender.put("receiver_id",receiverUserId);
        sender.put("request_type","sent");

        fStore.collection("ChatRequests").document(senderUserId)
                .set(sender)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> receiver = new HashMap<>();
                            receiver.put("sender_id",senderUserId);
                            receiver.put("request_type","received");

                            fStore.collection("ChatRequests").document(receiverUserId)
                                    .set(receiver)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sendMessageRequestButton.setEnabled(true);
                                                current_state = "request_sent";
                                                sendMessageRequestButton.setText("Cancel Chat Request");
                                            }
                                        }
                                    });

                    }}
                });

    }
}
