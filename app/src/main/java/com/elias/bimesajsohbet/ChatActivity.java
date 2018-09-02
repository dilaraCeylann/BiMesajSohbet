package com.elias.bimesajsohbet;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverId;
    private String messageReceiverName;

    private Toolbar KonusmaToolbar;
    private TextView userNameTitle;
    private TextView userLastSeen;
    private CircleImageView userChatProfileImage;

    private ImageButton SendFileButton;
    private ImageButton SendMessageButton;
    private EditText MesajGiris;

    private DatabaseReference rootRef;

    private FirebaseAuth mAuth;
    private String messageSenderId;
    String lastSeenDisplayTime;

    private RecyclerView userMessagesList;
    private final List<Messages> messagesList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    private MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();

        messageReceiverId =getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName=getIntent().getExtras().get("user_name").toString();

        KonusmaToolbar = findViewById(R.id.chat_bar_layout);
        setSupportActionBar(KonusmaToolbar);

        rootRef = FirebaseDatabase.getInstance().getReference();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,null);

        actionBar.setCustomView(action_bar_view);

        userNameTitle = findViewById(R.id.profil_ad);
        userLastSeen = findViewById(R.id.profile_son_gozukme);
        userChatProfileImage = findViewById(R.id.profile_resim);


        SendMessageButton = findViewById(R.id.mesaj_gonder);
        SendFileButton = findViewById(R.id.sec_buton);
        MesajGiris = findViewById(R.id.mesaj_giris);

        messageAdapter = new MessageAdapter(messagesList);
        userMessagesList = findViewById(R.id.mesaj_kullanıcı_liste);

        linearLayoutManager = new LinearLayoutManager(this);

        userMessagesList.setHasFixedSize(true);

        linearLayoutManager.setStackFromEnd(true);

        userMessagesList.setLayoutManager(linearLayoutManager);

        userMessagesList.setAdapter(messageAdapter);

        FetchMessages();
        userNameTitle.setText(messageReceiverName);





        rootRef.child("Users").child(messageReceiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String online = dataSnapshot.child("online").getValue().toString();
                final String userThumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                Picasso.with(ChatActivity.this).load(userThumbImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.standart_insan)
                        .into(userChatProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(ChatActivity.this).load(userThumbImage).placeholder(R.drawable.standart_insan).into(userChatProfileImage);
                            }
                        });

                if (InternetBaglanti.getInstance(getApplicationContext()).isOnline()) {
                    if (online.equals("true")) {
                        userLastSeen.setText("Şuanda aktif");
                    } else {

                        LastSeenTime getTime = new LastSeenTime();
                        long last_seen = Long.parseLong(online);

                        if (getTime.getTimeAgo(last_seen, getApplicationContext()) != null) {
                            lastSeenDisplayTime = getTime.getKalanZaman();
                            userLastSeen.setText(lastSeenDisplayTime);
                        } else {
                            userLastSeen.setText("");
                        }
                    }
                } else {
                    userLastSeen.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MesajGonder();
            }
        });



    }

    private void FetchMessages() {
        rootRef.child("Messages").child(messageSenderId).child(messageReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void MesajGonder() {
        String messageText = MesajGiris.getText().toString();

        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(ChatActivity.this, "Lütfen mesajınızı yazın.", Toast.LENGTH_SHORT).show();
        } else {
            String message_sender_ref = "Messages/" + messageSenderId + "/" + messageReceiverId;

            String message_receiver_ref = "Messages/" + messageReceiverId + "/" + messageSenderId;

            DatabaseReference user_message_key = rootRef.child("Messages").child(messageSenderId)
                    .child(messageReceiverId).push();

            String message_push_id = user_message_key.getKey();

            Map messageTextBody = new HashMap();

            messageTextBody.put("message",messageText);
            messageTextBody.put("seen",false);
            messageTextBody.put("type","text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from",messageSenderId);


            Map messageBodyDetails = new HashMap();

            messageBodyDetails.put(message_sender_ref + "/" + message_push_id,messageTextBody);

            messageBodyDetails.put(message_receiver_ref + "/" + message_push_id,messageTextBody);

            MesajGiris.setText("");

            rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d("Chat_Log",databaseError.getMessage().toString());
                    }
                }
            });

        }
    }
}
