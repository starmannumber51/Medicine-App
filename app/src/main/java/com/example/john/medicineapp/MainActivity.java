package com.example.john.medicineapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int ADD_CODE = 10;
    private static final int DELETE_CODE = 20;
    ArrayList<Medicine> MedList;

    TextView Medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReferenceUIComponents();
        MedList = new ArrayList<Medicine>(0);
    }

    public void ADD(View view) {
        Intent AddMedicines = new Intent(this, AddDialog.class);
        startActivityForResult(AddMedicines, ADD_CODE);
    }

    public void DELETE(View view) {
        Intent DeleteMedicines = new Intent(this, DeleteDialog.class);
        startActivityForResult(DeleteMedicines, DELETE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ADD_CODE) {
            if (data.hasExtra("Name")) {
                Medicine Med = new Medicine(data.getStringExtra("Name"), data.getIntExtra("Hour",12), data.getIntExtra("Doses",12));
                MedList.add(Med);
            }
        }
        if (resultCode == RESULT_OK && requestCode == DELETE_CODE) {
            if (data.hasExtra("Name")) {
                for (int i = 0; i < MedList.size(); i++) {
                    if (MedList.get(i).getName().equals(data.getStringExtra("Name"))) {
                        MedList.remove(i);
                        i--;
                    }
                }
            }
        }
        setText();
    }

    public void setText(){
        String MedicineList = "";
        if (!MedList.isEmpty()) {
            for (int i = 0; i < MedList.size(); i++) {
                MedicineList += MedList.get(i).getName() + ": Hour: " + MedList.get(i).getHour() + ": Doses: " +MedList.get(i).getNumberOfDoses() + "\n";
            }
        }
        Medicines.setText(MedicineList);
    }




    public void ReferenceUIComponents() {
        Medicines = findViewById(R.id.MedicineList);
    }
}
