package com.example.john.medicineapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by john on 11/19/17.
 */

public class DeleteDialog extends Activity {

    Intent data;
    EditText Name, Hour, Doses;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_medicine);
        ReferenceUIComponents();
        data = new Intent();
    }


    public void goodFinish() {
        setResult(RESULT_OK, data);
        finish();
    }

    public void ReferenceUIComponents() {
        Name = findViewById(R.id.DeleteName);
    }

    public void CancelDeletion(View view) {
        setResult(RESULT_CANCELED, data);
        finish();
    }

    public void DeleteMedicine(View view) {
        if (!(Name.getText().toString().isEmpty() )) {
            data.putExtra("Name", Name.getText().toString());
            goodFinish();
        }
        finish();
    }
}
