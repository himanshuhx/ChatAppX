package com.first75494.chatappx;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    private View privateChatsView;
    private RecyclerView chatsList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    public ChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        privateChatsView = inflater.inflate(R.layout.fragment_chat, container, false);

        chatsList = privateChatsView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return privateChatsView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Query query = firebaseFirestore.collection("Contacts");
        //todo     .where(currentUserId.toString(), "==", "saved").get();

        FirestoreRecyclerOptions<Contacts> options =
                new FirestoreRecyclerOptions.Builder<Contacts>()
                        .setQuery(query, Contacts.class)
                        .build();

        FirestoreRecyclerAdapter<Contacts, ChatsViewHolder> adapter =
                new FirestoreRecyclerAdapter<Contacts, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder  holder, final int position, @NonNull final Contacts model) {
                        final String userId = getSnapshots().getSnapshot(position).getId();

                        firebaseFirestore.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        final String username = documentSnapshot.getString("name");

                                        holder.userName.setText(username);
                                        holder.userStatus.setText("Last Seen: "+"\n"+"Date"+"Time");

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(),ChatActivity.class);
                                                intent.putExtra("visit_user_id",userId);
                                                intent.putExtra("visit_user_name",username);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i){
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        ChatsViewHolder  viewHolder = new ChatsViewHolder (view);
                        return viewHolder;
                    }

                };
        chatsList.setAdapter(adapter);

        adapter.startListening();
    }
    public static class ChatsViewHolder extends RecyclerView.ViewHolder{

        TextView userName,userStatus;
        CircleImageView profileImage;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }
}
