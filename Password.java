package com.example.a17011050;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Password extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        Button save = (Button) findViewById(R.id.button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String PASSWORD = "Test";
                EditText text = (EditText) findViewById(R.id.editText);
                int attempts = 4;
                String password = "";
                while (attempts-- > 0 && !PASSWORD.equals(password))
                {
                    password = text.getText().toString();
                    if (!password.equals(PASSWORD)) {
                        Toast.makeText(Password.this, "Yanlış Şifre!", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                    }
                }
            }
        });
    }
}
