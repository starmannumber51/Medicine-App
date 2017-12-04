package com.example.john.medicineapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by John Davis Jr on 12/3/2017.
 */

public class AddDoses extends Activity {

    Intent data;
    EditText Name, Number;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doses);
        ReferenceUIComponents();
        data = new Intent();
    }

    public void addDoses(View view) {
        if ( ! (Name.getText().toString().isEmpty() || Integer.valueOf(Number.getText().toString())<1 ) ){
            data.putExtra("Name", Name.getText().toString());
            data.putExtra("Number", Integer.valueOf(Number.getText().toString()));
            finish();
        }
        setResult(RESULT_CANCELED, data);
        finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void CancelDoses(View view) {
        setResult(RESULT_CANCELED, data);
        finish();
    }

    public void ReferenceUIComponents() {
        Name = findViewById(R.id.DoseName);
        Number = findViewById(R.id.DoseNumber);
    }
}
