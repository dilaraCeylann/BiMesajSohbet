package com.elias.bimesajsohbet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;



import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView usersList;
    private DatabaseReference allDatabaseUserreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolBar = findViewById(R.id.users_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Kullanıcılar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersList = findViewById(R.id.user_list);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));

        allDatabaseUserreference = FirebaseDatabase.getInstance().getReference().child("Users");
        allDatabaseUserreference.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Users, UsersViewHolder>
                (
                        Users.class,
                        R.layout.users_display_layout,
                        UsersViewHolder.class,
                        allDatabaseUserreference
                )
        {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, final int position) {
                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.getUser_status());
                viewHolder.setUser_thumb_image(getApplicationContext(),model.getUser_thumb_image());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        usersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUser_name(String user_name){

            TextView name = mView.findViewById(R.id.all_users_username);
            name.setText(user_name);
        }

        public void setUser_status(String user_status){
            TextView status = mView.findViewById(R.id.users_status);
            status.setText(user_status);
        }

        public void setUser_image(Context context, String user_image){
            CircleImageView image = mView.findViewById(R.id.users_profile_image);
            Picasso.with(context).load(user_image).into(image);
        }
        public void setUser_thumb_image(final Context context, final String user_thumb_image){
            final CircleImageView thumb_image = mView.findViewById(R.id.users_profile_image);

            Picasso.with(context).load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.standart_insan)
                    .into(thumb_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(user_thumb_image).placeholder(R.drawable.standart_insan).into(thumb_image);
                        }
                    });

        }
    }
}