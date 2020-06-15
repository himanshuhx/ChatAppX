package com.first75494.chatappx;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText userName,userStatus;
    private CircleImageView userProfileImage;
    private String currentUserId;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

      fStore = FirebaseFirestore.getInstance();
      fAuth=FirebaseAuth.getInstance();

        InitializeFields();

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });

    }

    private void InitializeFields() {
        updateAccountSettings = findViewById(R.id.update_settings_button);
        userName =  findViewById(R.id.set_user_name);
        userStatus =  findViewById(R.id.set_profile_status);
        userProfileImage =  findViewById(R.id.set_profile_image);
    }

    public void UpdateSettings() {

        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();

        currentUserId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(currentUserId);

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", setUserName);
        userMap.put("email", setStatus);

        documentReference.set(userMap, SetOptions.merge())
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
    }}