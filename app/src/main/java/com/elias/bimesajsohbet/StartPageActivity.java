package com.elias.bimesajsohbet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPageActivity extends AppCompatActivity {
    private Button NeedNewAccountButton;
    private Button AlreadyHaveAnAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        NeedNewAccountButton = findViewById(R.id.kayit);
        AlreadyHaveAnAccountButton = findViewById(R.id.giris);

        NeedNewAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartPageActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        AlreadyHaveAnAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(StartPageActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
