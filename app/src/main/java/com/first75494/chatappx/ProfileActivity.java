package com.first75494.chatappx;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private  String receiverUserId;

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

        documentReference = fStore.collection("users").document(receiverUserId);

        Toast.makeText(this,"User ID: "+receiverUserId,Toast.LENGTH_SHORT).show();

        userProfileImage =  findViewById(R.id.visit_profile_image);
        userProfileName = findViewById(R.id.visit_profile_name);
        sendMessageRequestButton = findViewById(R.id.send_message_request_button);
        userProfileEmail = findViewById(R.id.visit_profile_email);

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
                        }else {
                            Toast.makeText(ProfileActivity.this,"User doesn't have data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
