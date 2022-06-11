package com.example.a17011050;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.notes.MESSAGE";
    String feeling = "";
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        feeling = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File files = getFilesDir();
        String[] array = files.list();
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        for (String filename : array) {
            filename = filename.replace(".txt", "");
            System.out.println(filename);
            adapter.add(filename);
        }
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.feelings, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(this);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.savebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextHeading = (EditText) findViewById(R.id.editTextName);
                EditText editTextContent = (EditText) findViewById(R.id.contentfield);
                TextView textDateTime = (TextView) findViewById(R.id.textDateTime);
                textDateTime.setVisibility(View.INVISIBLE);
                textDateTime.setText(
                        new SimpleDateFormat("EEEE, dd, MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
                );

                String heading = editTextHeading.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                String date = textDateTime.getText().toString().trim();
                if (!heading.isEmpty()) {
                    if(!content.isEmpty()) {
                        try {
                            FileOutputStream fileOutputStream = openFileOutput(heading + ".txt", Context.MODE_PRIVATE);
                            fileOutputStream.write(content.getBytes());
                            String line = "\n-\n";
                            fileOutputStream.write(line.getBytes());
                            fileOutputStream.write(date.getBytes());
                            fileOutputStream.write(line.getBytes());
                            fileOutputStream.write(feeling.getBytes());
                            fileOutputStream.close();
                            Toast.makeText(MainActivity.this, "Anı Kaydedildi!", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        adapter.add(heading);
                        listView.setAdapter(adapter);
                    }else {
                        editTextContent.setError("İçerik boş kalamaz!");
                    }
                }else{
                    editTextHeading.setError("Başlık boş kalamaz!");
                }
                editTextContent.setText("");
                editTextHeading.setText("");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), Note.class);
                intent.putExtra(EXTRA_MESSAGE, item);
                startActivity(intent);
            }
        });
    }


}