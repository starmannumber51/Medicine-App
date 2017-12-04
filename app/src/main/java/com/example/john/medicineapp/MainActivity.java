package com.example.john.medicineapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    boolean notify;
    boolean notify2;

    Calendar rightNow = Calendar.getInstance();

    private static final int ADD_CODE = 10;
    private static final int DELETE_CODE = 20;
    private static final int DOSE_CODE = 30;
    int i = 0;

    MedicineList myList;
    ArrayList<Medicine> MedList;

    TextView Medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReferenceUIComponents();
        myList = new MedicineList();
        myList.Medlist = new ArrayList<Medicine>(0);
        killme();
        notify2 = true;
        notify = true;
        Load();
        setText();
    }

    public void ADD(View view) {
        Intent AddMedicines = new Intent(this, AddDialog.class);
        killalarm(false);
        startActivityForResult(AddMedicines, ADD_CODE);
    }

    public void DELETE(View view) {
        Intent DeleteMedicines = new Intent(this, DeleteDialog.class);
        killalarm(false);
        startActivityForResult(DeleteMedicines, DELETE_CODE);
    }

    public void DOSE(View view) {
        Intent AddDoses = new Intent(this, AddDoses.class);
        killalarm(false);
        startActivityForResult(AddDoses, DOSE_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ADD_CODE) {
            if (data.hasExtra("Name")) {
                Medicine Med = new Medicine(data.getStringExtra("Name"), data.getIntExtra("Hour",0), data.getIntExtra("Doses",0));
                myList.Medlist.add(Med);
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                if (Med.getHour() == hour) {
                    notify = false;
                    notify2 = false;
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == DELETE_CODE) {
            if (data.hasExtra("Name")) {
                for (int i = 0; i < myList.Medlist.size(); i++) {
                    if (myList.Medlist.get(i).getName().equals(data.getStringExtra("Name"))) {
                        myList.Medlist.remove(i);
                        i--;
                    }
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == DOSE_CODE) {
            if (data.hasExtra("Name")) {
                for (int i = 0; i < myList.Medlist.size(); i++) {
                    if (myList.Medlist.get(i).getName().equals(data.getStringExtra("Name"))) {
                        myList.Medlist.get(i).setNumberOfDoses(myList.Medlist.get(i).getNumberOfDoses()+ data.getIntExtra("Number", 0));
                    }
                }
            }
        }
        setText();
        //AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, Time.class);

        ArrayList<String> names;
        ArrayList<Integer> times, doses;
        names = new ArrayList<String>(0);
        doses = new ArrayList<Integer>(0);
        times = new ArrayList<Integer>(0);
        for (int j =0; j < myList.Medlist.size(); j++) {
            names.add(myList.Medlist.get(j).getName());
            doses.add (myList.Medlist.get(j).getNumberOfDoses());
            times.add(myList.Medlist.get(j).getHour());
        }
        killme();
    }

    public void setText(){
        String MedicineList = "";
        if (!myList.Medlist.isEmpty()) {
            for (int i = 0; i < myList.Medlist.size(); i++) {
                MedicineList += myList.Medlist.get(i).getName() + ": Hour: " + myList.Medlist.get(i).getHour() + ": Doses: " +myList.Medlist.get(i).getNumberOfDoses() + "\n";
            }
        }
        Medicines.setText(MedicineList);
        Save();
    }


    public void killme() {
        Intent intent= new Intent(this, Time.class);

        intent.putExtra("List", myList);
        Log.d("Notify1", String.valueOf(notify));
        Log.d("Notify2", String.valueOf(notify2));
        intent.putExtra("notify1", notify);
        intent.putExtra("notify2", notify2);
        intent.putExtra("listener", new ResultReceiver(new Handler()) {
            @Override
            protected  void onReceiveResult (int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);

                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> values = resultData.getStringArrayList("taken");
                    for (int i = 0; i < values.size(); i++) {
                        Log.d("TAKEN", values.get(i));
                        for (int j = 0; j < myList.Medlist.size(); j++) {
                            if (myList.Medlist.get(j).getName().equals(values.get(i)))
                                myList.Medlist.get(j).setNumberOfDoses(myList.Medlist.get(j).getNumberOfDoses() - 1);
                        }
                    }
                }
                notify = resultData.getBoolean("notify");
                notify2 = resultData.getBoolean("notify2");
                Log.d("Notify1", String.valueOf(notify));
                Log.d("Notify2", String.valueOf(notify2));
                setText();
                killalarm(true);
            }
        });
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+ 1000, 1000, pi);
    }

    public void killalarm(boolean reset) {
        Intent intent = new Intent(this, Time.class);
        PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
        if (reset) {
            killme();
        }
    }

    public void Save() {
        String text = "";
        for (int i = 0; i < myList.Medlist.size(); i++) {
            text += myList.Medlist.get(i).getName() + "\n" + String.valueOf(myList.Medlist.get(i).getHour()) + "\n" + String.valueOf(myList.Medlist.get(i).getNumberOfDoses())+ "\n";
        }
        try {
            FileOutputStream fileOut = openFileOutput("mytextfile.txt",MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fileOut);
            writer.write(text);
            writer.close();
            Toast.makeText(this, "Medicine list saved successfully!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Could not find file to write to", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Could not find write to file", Toast.LENGTH_LONG).show();
        }
    }

    public void Load () {
        String readString;
        try{
            FileInputStream fileIn = openFileInput("mytextfile.txt");
            InputStreamReader InputRead = new InputStreamReader((fileIn));
            BufferedReader bR = new BufferedReader(InputRead);
            while ((readString = bR.readLine())!= null){
                Medicine med = new Medicine(readString, Integer.valueOf(bR.readLine()), Integer.valueOf(bR.readLine()));
                myList.Medlist.add(med);
            }
            InputRead.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No File Found", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Could not read from file", Toast.LENGTH_LONG).show();
        }
    }



    public void ReferenceUIComponents() {
        Medicines = findViewById(R.id.MedicineList);
    }

}
