package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.carrier.MessagePdu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText className,number;
    Button addClass;
    ListView classes;
    ArrayList<String> classList= new ArrayList<>();
    ArrayList<Integer> noOfStudents= new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    int numberOfStudents=0;
    SQLiteDatabase attendanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attendanceManager = this.openOrCreateDatabase("attendance", MODE_PRIVATE,null);
        attendanceManager.execSQL("CREATE TABLE IF NOT EXISTS student(name VARCHAR,rollNo INT,cName VARCHAR, PRIMARY KEY(rollNo,cName), FOREIGN KEY(cName) REFERENCES class(cName))");
        attendanceManager.execSQL("CREATE TABLE IF NOT EXISTS class(cName VARCHAR,students INT,PRIMARY KEY(cName))");
        attendanceManager.execSQL("CREATE TABLE IF NOT EXISTS leave(rollNo VARCHAR,days INT,FOREIGN KEY(rollNo) REFERENCES students(rollNo))");


        className=findViewById(R.id.className);
        classes=findViewById(R.id.classes);
        number=findViewById(R.id.number);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,classList);
        classes.setAdapter(arrayAdapter);

        updateListView();

        classes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),Attendance.class);
                numberOfStudents=noOfStudents.get(position);
                intent.putExtra("number",numberOfStudents);
                intent.putExtra("class",classList.get(position));
                startActivity(intent);
            }
        });

    }


    public void addClass(View v)
    {

        attendanceManager.execSQL("INSERT INTO class VALUES ('" + className.getText().toString() + "',"+ Integer.parseInt(number.getText().toString())+ ")");
        classList.add(className.getText().toString());
        noOfStudents.add(Integer.parseInt(number.getText().toString()));
        arrayAdapter.notifyDataSetChanged();
        className.setText("");
        number.setText("");
    }

    public void updateListView() {
        Cursor c = attendanceManager.rawQuery("SELECT * FROM class", null);

        int classIndex = c.getColumnIndex("cName");
        int studentsIndex = c.getColumnIndex("students");

        if (c.moveToFirst()) {
            classList.clear();
            noOfStudents.clear();

            do {
                classList.add(c.getString(classIndex));
                noOfStudents.add(c.getInt(studentsIndex));
            } while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }
    }

}
