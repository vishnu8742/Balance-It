package com.vishnu.anon.balanceit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class add_bank extends AppCompatActivity {
    Button submit;
    EditText bank_name;
    EditText service_name;
    RelativeLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank);
        bank_name = findViewById(R.id.bank_name);
        service_name = findViewById(R.id.service_name);
        layout = (RelativeLayout) findViewById(R.id.relative_layout);
        submit = findViewById(R.id.bank_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String bank = bank_name.getText().toString().trim();
              String service = service_name.getText().toString().trim();
              if(bank.isEmpty() && service.isEmpty()){
                  Toast.makeText(getApplicationContext(), "Dont Treat Me Like A Stupid", Toast.LENGTH_LONG).show();
              }else if(bank.isEmpty() && !service.isEmpty()){
                  Toast.makeText(getApplicationContext(), "You Entered "+service+" Service", Toast.LENGTH_LONG).show();
              }else if (!bank.isEmpty() && service.isEmpty()){
                  Toast.makeText(getApplicationContext(), "You Entered "+bank+" Bank", Toast.LENGTH_LONG).show();
              }else if (!bank.isEmpty() && !service.isEmpty()){
                  Toast.makeText(getApplicationContext(), "Bank : "+bank+" Service : " +service, Toast.LENGTH_LONG).show();
              }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
