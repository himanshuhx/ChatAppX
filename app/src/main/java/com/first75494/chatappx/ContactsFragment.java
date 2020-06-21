package com.first75494.chatappx;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import de.hdodenhof.circleimageview.CircleImageView;


public class ContactsFragment extends Fragment {

    private View contactsView;
    private RecyclerView contactsList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    public ContactsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

         contactsView = inflater.inflate(R.layout.fragment_contacts, container, false);

         contactsList = contactsView.findViewById(R.id.contacts_list);
         contactsList.setLayoutManager(new LinearLayoutManager(getContext()));

         return contactsView;
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

        FirestoreRecyclerAdapter<Contacts, ContactsViewHolder> adapter =
                new FirestoreRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position, @NonNull final Contacts model) {
                        String userId = getSnapshots().getSnapshot(position).getId();

                        firebaseFirestore.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String username = documentSnapshot.getString("name");
                                        String email = documentSnapshot.getString("email");

                                        holder.userName.setText(username);
                                        holder.userStatus.setText(email);
                                    }
                                });
                }

        @NonNull
        @Override
        public ContactsViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup,int i){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
            ContactsViewHolder viewHolder = new ContactsViewHolder(view);
            return viewHolder;
         }

      };
        contactsList.setAdapter(adapter);

        adapter.startListening();
    }
    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView userName,userStatus;
        CircleImageView profileImage;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }
}
