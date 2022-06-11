package com.example.a17011050;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    int pageHeight = 1120;
    int pagewidth = 792;

    String whole = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Button generate_pdf = findViewById(R.id.generate_pdf);
        if (!checkPermission()) {
            requestPermission();
        }

        final Intent intent = getIntent();
        final String heading = intent.getStringExtra(Note.HEADER_MSG);
        try {
            FileInputStream fis = openFileInput(heading + ".txt");
            BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );
            String line;
            line = reader.readLine();
            while ( !line.equals("-") ) {
                if(whole == "") {
                    whole = whole + line;
                }else{
                    whole = whole + "\n" + line;
                }
                line = reader.readLine();
            }
            reader.close();
            final EditText editText = (EditText) findViewById(R.id.contentfield);
            editText.setText(whole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditText editText = (EditText) findViewById(R.id.contentfield);
        TextView textView = (TextView) findViewById(R.id.heading_view);
        textView.setText(heading);
        Button button_save = (Button) findViewById(R.id.savebutton);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = heading + ".txt";
                if(!editText.getText().toString().trim().isEmpty()){
                    File dir = getFilesDir();
                    File file = new File(dir, filename);
                    file.delete();
                    try {
                        FileOutputStream fOut = openFileOutput(filename, Context.MODE_PRIVATE);
                        fOut.write(editText.getText().toString().trim().getBytes());
                        fOut.close();
                        TextView status_view = (TextView) findViewById(R.id.status);
                        Toast.makeText(EditActivity.this, "Anı Düzenlendi!", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    editText.setError("İçerik boş kalamaz!");
                }
            }
        });
        Button button = (Button) findViewById(R.id.back_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });
        Button generatePdf = (Button) findViewById(R.id.generate_pdf);
        generatePdf.setOnClickListener((view)->{
            PdfDocument myPdf = new PdfDocument();
            Paint myPaint = new Paint();
            Paint titlePaint = new Paint();
            Paint contentPaint = new Paint();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
            PdfDocument.Page page = myPdf.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            titlePaint.setTextSize(70);
            contentPaint.setTextAlign(Paint.Align.CENTER);
            contentPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            contentPaint.setTextSize(35);
            canvas.drawText("Anı",pagewidth/2,270,titlePaint);
            canvas.drawText(whole,pagewidth/2,370,contentPaint);

            myPaint.setColor(Color.rgb(110,20,90));
            myPaint.setTextSize(30f);
            myPdf.finishPage(page);
            File file = new File(Environment.getExternalStorageDirectory(),"memo.pdf");
            try{
                myPdf.writeTo(new FileOutputStream(file));
                Toast.makeText(this, "Pdf Oluşturuldu!", Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
            myPdf.close();
        });

    }
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "İzin verildi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "İzin reddedildi", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}