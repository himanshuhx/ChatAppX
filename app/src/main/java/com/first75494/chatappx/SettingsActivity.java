package com.first75494.chatappx;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText userName,userEmail;
    private CircleImageView userProfileImage;
    private FirebaseFirestore  fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private DocumentReference documentReference;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userid = fAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("user").document(userid);

        updateAccountSettings = findViewById(R.id.update_settings_button);
        userName =  findViewById(R.id.set_user_name);
        userEmail =  findViewById(R.id.set_profile_email);
        userProfileImage =  findViewById(R.id.set_profile_image);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userName.setText(documentSnapshot.getString("name"));
                userEmail.setText(documentSnapshot.getString("email"));
            }
        });

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setUserName = userName.getText().toString();
                String setEmail = userEmail.getText().toString();

                DocumentReference documentReference = fStore.collection("users").document(userid);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("name", setUserName);
                userMap.put("email", setEmail);

                documentReference.update(userMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}