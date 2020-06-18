package com.first75494.chatappx;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private Toolbar settingsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userid = fAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("user").document(userid);

        FirebaseUser user = fAuth.getCurrentUser();

        updateAccountSettings = findViewById(R.id.update_settings_button);
        userName =  findViewById(R.id.set_user_name);
        userEmail =  findViewById(R.id.set_profile_email);
        userProfileImage =  findViewById(R.id.set_profile_image);

        settingsToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile Settings");

        if(user.getDisplayName() != null ){
            userName.setText(user.getDisplayName());
        }
        if(user.getEmail()!= null){
            userEmail.setText(user.getEmail());
        }

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
                                Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}