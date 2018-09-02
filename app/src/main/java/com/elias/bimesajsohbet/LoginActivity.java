package com.elias.bimesajsohbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private EditText LoginEmail;
    private EditText LoginPassword;
    private ProgressDialog loadingBar;

    private DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Giriş Yap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoginButton = findViewById(R.id.login_button);
        LoginEmail = findViewById(R.id.login_email);
        LoginPassword = findViewById(R.id.login_password);



        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = LoginEmail.getText().toString();
                String password = LoginPassword.getText().toString();

                LoginUserAccount (email,password);
            }
        });
    }

    private void LoginUserAccount(String email, String password){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Lütfen emailinizi giriniz.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Lütfen şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Giriş yapılıyor.");
            loadingBar.setMessage("Lütfen giriş yaparken bekleyiniz.");
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task)
                       {
                           if (task.isSuccessful()){
                               String online_user_id = mAuth.getCurrentUser().getUid();
                               String Device_Token = FirebaseInstanceId.getInstance().getToken();

                               usersReference.child(online_user_id).child("device_token").setValue(Device_Token)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                               mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                               startActivity(mainIntent);
                                               finish();
                                           }
                                       });

                           }
                           else
                           {
                               Toast.makeText(LoginActivity.this,
                                       "Yanlış email veya şifre. Lütfen yeniden deneyiniz.", Toast.LENGTH_SHORT).show();
                           }
                           loadingBar.dismiss();
                       }
                    });
        }

    }
}
