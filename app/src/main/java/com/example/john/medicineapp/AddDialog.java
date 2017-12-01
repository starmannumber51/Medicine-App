package com.example.john.medicineapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by john on 11/19/17.
 */

public class AddDialog extends Activity {

    EditText Name, Hour, Doses;
    Intent data;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine);
        ReferenceUIComponents();
        data = new Intent();
    }

    public void AddMedicine(View view) {
        if (!(Name.getText().toString().isEmpty() || Hour.getText().toString().isEmpty() || Doses.getText().toString().isEmpty())) {
             data.putExtra("Name", Name.getText().toString());
             data.putExtra("Hour", Integer.valueOf(Hour.getText().toString()));
             data.putExtra("Doses", Integer.valueOf(Doses.getText().toString()));
            finish();
        }
        finish();
    }

    public void CancelAddition(View view) {
        finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void ReferenceUIComponents() {
        Name = findViewById(R.id.nameInput);
        Hour = findViewById(R.id.oftenInput);
        Doses = findViewById(R.id.dosesInput);
    }
}
