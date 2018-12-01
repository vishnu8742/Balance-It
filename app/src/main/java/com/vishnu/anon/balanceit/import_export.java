package com.vishnu.anon.balanceit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class import_export extends AppCompatActivity {
    Button imports;
    Button exports;
    private static final int REQUEST_PERMISSION_WRITE = 101 ;
    private boolean permissionGranted;
    String db_name = "balance_it.db";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_export);
        getSupportActionBar().setTitle("Import/Export");
        imports = (Button)findViewById(R.id.importt);
        exports = (Button)findViewById(R.id.exportt);
        imports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB();
            }
        });
        exports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB();
            }
        });

        if(!permissionGranted) {
            checkPermissions();
        }
    }


    private void importDB() {
        String DATABASE_NAME = "balance_it.db";
        String databasePath = import_export.this.getDatabasePath(DATABASE_NAME).getPath();
        String inFileName = Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME;
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = databasePath;

            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Close the streams
            output.flush();
            output.close();
            fis.close();
            Toast.makeText(import_export.this, "Import Successful!",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(import_export.this, "Import Failed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void  exportDB() {
        String DATABASE_NAME = "balance_it.db";
        String databasePath = import_export.this.getDatabasePath(DATABASE_NAME).getPath();
        String inFileName = databasePath;
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME;

            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Close the streams
            output.flush();
            output.close();
            fis.close();
            Toast.makeText(import_export.this, "Export Successful!",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Exception", "exportDB: " + e);
            Toast.makeText(import_export.this, "Export Failed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "పర్మిషన్ లేకపోతే ఫైల్ ఎలా సేవ్ చెయ్యాలి చెప్పు ???",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "ఇచ్చేసావా , ఇంకెందుకు ఆలస్యం.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "పర్మిషన్ ఇవ్వకపోతే BACK UP తీస్కోడం కుదరదు ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(import_export.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}