package com.elias.bimesajsohbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private Button SaveChangeButton;
    private EditText statusInput;

    private ProgressDialog loadingBar;

    private DatabaseReference changeStatusRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        changeStatusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mToolBar = findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Durumunu güncelle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SaveChangeButton = findViewById(R.id.save_status_change_button);
        statusInput = findViewById(R.id.status_input);

        loadingBar = new ProgressDialog(this);

        String old_status = getIntent().getExtras().get("user_status").toString();
        statusInput.setText(old_status);

        SaveChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String new_status = statusInput.getText().toString();

                ChangeProfileStatus(new_status);
            }
        });
    }

    private void ChangeProfileStatus(String new_status) {
        if(TextUtils.isEmpty(new_status)){
            Toast.makeText(this, "Lütfen durumunuzu boş bırakmayınız.", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Durumunuzu güncelleyin.");
            loadingBar.setMessage("Lütfen bekleyiniz...");

            changeStatusRef.child("user_status").setValue(new_status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                loadingBar.dismiss();

                                Intent settingsIntent = new Intent(StatusActivity.this,SettingsActivity.class);
                                startActivity(settingsIntent);

                                Toast.makeText(StatusActivity.this, "Durum başarıyla güncellendi.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StatusActivity.this, "Hata oldu! Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
