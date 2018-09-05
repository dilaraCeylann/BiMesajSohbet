package com.elias.bimesajsohbet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class RequestsFragment extends Fragment {


    private RecyclerView myRequestList;

    private View myMainView;

    private DatabaseReference FriendsRequesstsReference;
    private FirebaseAuth mAuth;
    String online_user_id;

    private DatabaseReference UsersReference;



    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        myMainView = inflater.inflate(R.layout.fragment_requests, container, false);

        myRequestList = myMainView.findViewById(R.id.request_list);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();

        FriendsRequesstsReference = FirebaseDatabase.getInstance().getReference().child("Friend_Requests").child(online_user_id);
        UsersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        myRequestList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        myRequestList.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment
        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Requests,RequestViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Requests, RequestViewHolder>
                (
                        Requests.class,
                        R.layout.friend_request_all_users_layout,
                        RequestsFragment.RequestViewHolder.class,
                        FriendsRequesstsReference

                )
        {
            @Override
            protected void populateViewHolder(final RequestViewHolder viewHolder, Requests model, int position) {
                final String list_users_id = getRef(position).getKey();
                UsersReference.child(list_users_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("user_name").getValue().toString();
                        final String thumb_images = dataSnapshot.child("user_thumb_image").getValue().toString();
                        final String userStatus = dataSnapshot.child("user_status").getValue().toString();

                        viewHolder.setUserName(userName);
                        viewHolder.setThumb_user_image(thumb_images,getContext());
                        viewHolder.setUserStatus(userStatus);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        };
        myRequestList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public RequestViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUserName(String userName) {
            TextView userNameDisplay = mView.findViewById(R.id.request_profile_name);
            userNameDisplay.setText(userName);
        }

        public void setThumb_user_image(final String thumb_images,final Context context) {
            final CircleImageView thumb_image = mView.findViewById(R.id.request_profile_image);

            Picasso.with(context).load(thumb_images).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.standart_insan)
                    .into(thumb_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(thumb_images).placeholder(R.drawable.standart_insan).into(thumb_image);
                        }
                    });
        }

        public void setUserStatus(String userStatus) {
            TextView status = mView.findViewById(R.id.request_profile_status);
            status.setText(userStatus);
        }
    }


}
