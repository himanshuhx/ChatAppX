package com.first75494.chatappx;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;  //Declaring the variables
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private FirebaseUser currentUser;
    private FirebaseAuth fAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.mytoolbar); //Finding toolbar id
        setSupportActionBar(toolbar); //Setting the ToolBar As The AppBar For The Activity
        getSupportActionBar().setTitle("ChatAppX"); //Setting Up The Title Of Our Action Bar

        viewPager = (ViewPager) findViewById(R.id.myviewpager); //Finding viewpager id
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager()); //calling the android Fragment Manager
        viewPager.setAdapter(viewPagerAdapter); //Binding the viewpagerAdapter

        tabLayout = (TabLayout) findViewById(R.id.tablayout); //Finding tabLayout id
        tabLayout.setupWithViewPager(viewPager); //Setting up with our view
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
          super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.main_logout_option){
            fAuth.signOut();
            SendUserToLoginActivity();
         }
        if(item.getItemId() == R.id.main_settings_option){
             SendUserToSettingsActivity();
        }
        if(item.getItemId() == R.id.main_find_friends_option){
               SendUserToFindFriendsActivity();
        }
        return true;
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this,loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void SendUserToSettingsActivity() {
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    private void SendUserToFindFriendsActivity() {
        Intent intent = new Intent(MainActivity.this,FindFriends.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            SendUserToLoginActivity();
        }else{
            //VerifyUserExistence();
            Toast.makeText(MainActivity.this,"No Username",Toast.LENGTH_SHORT).show();
        }
    }

  /*  private void VerifyUserExistence() {
        String currentUserId = fAuth.getCurrentUser().getUid();

        rootRef.child("users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())){
                    Toast.makeText(MainActivity.this,"Hello There",Toast.LENGTH_SHORT).show();
                }else{
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
