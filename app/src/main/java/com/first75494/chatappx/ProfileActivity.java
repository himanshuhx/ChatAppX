package com.first75494.chatappx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private  String receiverUserId,senderUserId,current_state;

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
        sender.put("request_type","sent");

        fStore.collection("ChatRequests")
                .add(sender)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                               if(task.isSuccessful()){
                                   Map<String,Object> receiver = new HashMap<>();
                                   receiver.put("request_type","received");

                                   fStore.collection("ChatRequests")
                                           .add(receiver)
                                           .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentReference> task) {
                                                   if(task.isSuccessful()){
                                                       sendMessageRequestButton.setEnabled(true);
                                                       current_state = "request_sent";
                                                       sendMessageRequestButton.setText("Cancel Chat Request");
                                                   }
                                               }
                                           });
                               }
                    }
                });

    }
}
